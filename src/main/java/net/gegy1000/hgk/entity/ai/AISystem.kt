package net.gegy1000.hgk.entity.ai

import net.gegy1000.hgk.entity.Player
import net.gegy1000.hgk.entity.ai.goal.Goal
import net.gegy1000.hgk.entity.ai.goal.GoalData
import net.gegy1000.hgk.entity.ai.goal.GoalType
import net.gegy1000.hgk.entity.ai.navigation.Navigator
import java.util.EnumMap

class AISystem(val player: Player) {
    val navigator = Navigator(player)

    var moved = false

    private var activeGoal: Goal? = null
    private val goalTypes = EnumMap<GoalType, Goal>(GoalType::class.java)

    init {
        GoalType.values().forEach { goalTypes.put(it, it.create(player)) }
    }

    fun update() {
        moved = false

        if (player.sleepTime <= 0) {
            updateRequirements()

            val goal = activeGoal

            if (goal != null) {
                if (goal.updateDependencies() && (goal.fulfilled || goal.failed)) {
                    activeGoal = null
                    if (!goal.failed) {
                        player.logger.info("${player.info.name} completed goal ${goal.type}")
                    } else {
                        player.logger.info("${player.info.name} failed goal ${goal.type}")
                    }
                }
            }
        }
    }

    private fun updateRequirements() {
        val requirements = Requirement.values().sortedByDescending {
            val weight = it.baseWeight * it.weightProvider(player)
            if (weight >= it.minThreshold) weight else -1.0F
        }
        requirements.firstOrNull()?.let { requirement ->
            if (activeGoal?.referrer != requirement) {
                if (requirement.baseWeight * requirement.weightProvider(player) >= requirement.minThreshold) {
                    player.logger.info("${player.info.name} starting goal ${requirement.goal} from $requirement requirement")
                    executeGoal(getGoal(requirement.goal), requirement.createInput(), requirement)
                }
            }
        }
    }

    private fun executeGoal(goal: Goal, input: GoalData, referrer: Requirement? = null) {
        goal.reset(input, referrer)

        activeGoal = goal

        goal.start(input)
    }

    fun getGoal(type: GoalType): Goal = goalTypes[type] ?: throw IllegalStateException("could not find goal for $type")

}
