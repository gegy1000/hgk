package net.gegy1000.hgk.entity.ai.goal

import net.gegy1000.hgk.entity.Player
import net.gegy1000.hgk.entity.ai.Requirement
import java.util.concurrent.LinkedBlockingDeque

abstract class Goal(val player: Player, val type: GoalType) {
    open var failed: Boolean = false

    var referrer: Requirement? = null
    var input: GoalData? = null

    val dependencyQueue = LinkedBlockingDeque<Dependency>()
    var activeDependency: Dependency? = null

    abstract val fulfilled: Boolean

    fun reset(input: GoalData, referrer: Requirement?) {
        this.activeDependency = null
        this.dependencyQueue.clear()
        this.input = input
        this.referrer = referrer
        this.failed = false
    }

    abstract fun start(input: GoalData)

    fun updateDependencies(): Boolean {
        var dependency = activeDependency
        if (dependency == null) {
            if (dependencyQueue.isEmpty()) {
                update(input ?: return true)
                return dependencyQueue.isEmpty()
            } else {
                dependency = dependencyQueue.poll()

                val goal = player.ai.getGoal(dependency.type)

                goal.reset(dependency.input, referrer)

                player.logger.info("${player.info.name} starting dependency goal ${dependency.type}")
                goal.start(dependency.input)

                if (goal.failed) {
                    dependency.complete(false)
                } else {
                    activeDependency = dependency
                }
            }
        } else {
            val goal = player.ai.getGoal(dependency.type)
            if (goal.updateDependencies() && (goal.fulfilled || goal.failed)) {
                activeDependency = null
                dependency.complete(!goal.failed)
                return true
            }
        }
        return false
    }

    abstract fun update(input: GoalData)

    protected fun call(type: GoalType, input: GoalData = GoalData(), complete: (Boolean) -> Unit = {}) {
        dependencyQueue.add(Dependency(type, input, complete))
    }

    data class Dependency(val type: GoalType, val input: GoalData, val complete: (Boolean) -> Unit)
}
