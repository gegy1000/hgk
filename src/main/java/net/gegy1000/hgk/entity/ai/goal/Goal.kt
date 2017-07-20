package net.gegy1000.hgk.entity.ai.goal

import net.gegy1000.hgk.entity.Player
import net.gegy1000.hgk.entity.ai.Requirement
import java.util.Queue
import java.util.concurrent.LinkedBlockingDeque

abstract class Goal(val player: Player, val type: Goal.Type) {
    open var failed: Boolean = false

    var referrer: Requirement? = null
    var input: GoalData? = null

    val dependencyQueue: Queue<Dependency> = LinkedBlockingDeque()
    var activeDependency: Dependency? = null

    abstract val fulfilled: Boolean

    abstract fun start(input: GoalData)

    fun updateDependencies(): Boolean {
        var dependency = activeDependency
        if (dependency == null) {
            if (dependencyQueue.isEmpty()) {
                update(input ?: return true)
                return dependencyQueue.isEmpty()
            } else {
                dependency = dependencyQueue.poll()

                val goal = player.getGoal(dependency.type)

                goal.activeDependency = null
                goal.dependencyQueue.clear()
                goal.referrer = referrer
                goal.input = dependency.input

                player.logger.info("${player.name} starting dependency goal ${dependency.type}")
                goal.start(dependency.input)

                if (goal.failed) {
                    dependency.complete(false)
                } else {
                    activeDependency = dependency
                }
            }
        } else {
            val goal = player.getGoal(dependency.type)
            if (goal.updateDependencies() && (goal.fulfilled || goal.failed)) {
                dependency.complete(!goal.failed)
                activeDependency = null
                return true
            }
        }
        return false
    }

    abstract fun update(input: GoalData)

    protected fun call(type: Type, input: GoalData = GoalData(), complete: (Boolean) -> Unit = {}) {
        dependencyQueue.add(Dependency(type, input, complete))
    }

    enum class Type {
        MOVE_TO {
            override fun create(player: Player) = MoveToGoal(player)
        },
        FOLLOW_PATH {
            override fun create(player: Player) = FollowPathGoal(player)
        },
        FIND_WATER {
            override fun create(player: Player) = FindWaterGoal(player)
        },
        FIND_FOOD {
            override fun create(player: Player) = FindFoodGoal(player)
        },
        HARVEST_FOOD {
            override fun create(player: Player) = HarvestFoodGoal(player)
        },
        REST {
            override fun create(player: Player) = RestGoal(player)
        };

        abstract fun create(player: Player): Goal
    }

    data class Dependency(val type: Type, val input: GoalData, val complete: (Boolean) -> Unit)
}
