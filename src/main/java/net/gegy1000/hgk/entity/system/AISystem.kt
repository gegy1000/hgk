package net.gegy1000.hgk.entity.system

import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.EntityFamily
import net.gegy1000.hgk.entity.ai.PlayerRequirement
import net.gegy1000.hgk.entity.ai.goal.Goal
import net.gegy1000.hgk.entity.ai.goal.GoalData
import net.gegy1000.hgk.entity.ai.goal.GoalType
import net.gegy1000.hgk.entity.component.AIComponent
import net.gegy1000.hgk.entity.component.LivingComponent
import net.gegy1000.hgk.entity.component.ReferralComponent
import net.gegy1000.hgk.entity.component.SleepComponent
import net.gegy1000.hgk.notNull

class AISystem : EntitySystem {
    override val family = EntityFamily.all(AIComponent::class)

    override fun update(entity: Entity) {
        val ai = entity[AIComponent::class]
        val living = entity.getOrNull(LivingComponent::class)
        val sleep = entity.getOrNull(SleepComponent::class)

        if (!living?.dead.notNull() && (sleep?.sleepTime ?: 0) <= 0) {
            updateRequirements(entity)

            ai.activeGoal?.let { goal ->
                if (updateDependencies(entity, goal) && (goal.fulfilled || goal.failed)) {
                    ai.activeGoal = null
                    if (entity.hasComponent(ReferralComponent::class)) {
                        val referral = entity[ReferralComponent::class]
                        if (!goal.failed) {
                            entity.logger.info("${referral.name} completed goal ${goal.type}")
                        } else {
                            entity.logger.info("${referral.name} failed goal ${goal.type}")
                        }
                    }
                }
            }
        }
    }

    private fun updateRequirements(entity: Entity) {
        val ai = entity[AIComponent::class]
        val requirements = PlayerRequirement.values().sortedByDescending {
            val weight = it.baseWeight * it.weightProvider(entity)
            if (weight >= it.minThreshold || ai.activeGoal?.type == it.goal) weight else -1.0F
        }
        requirements.firstOrNull()?.let { requirement ->
            if (ai.activeGoal?.referrer != requirement) {
                if (requirement.baseWeight * requirement.weightProvider(entity) >= requirement.minThreshold) {
                    if (entity.hasComponent(ReferralComponent::class)) {
                        entity.logger.info("${entity[ReferralComponent::class].name} starting goal ${requirement.goal} from $requirement requirement")
                    }
                    executeGoal(entity, getGoal(entity, requirement.goal), requirement.createInput(), requirement)
                }
            }
        }
    }

    private fun updateDependencies(entity: Entity, goal: Goal): Boolean {
        var dependency = goal.activeDependency
        if (dependency == null) {
            if (goal.dependencyQueue.isEmpty()) {
                goal.update(goal.input ?: return true)
                return goal.dependencyQueue.isEmpty()
            } else {
                dependency = goal.dependencyQueue.poll()

                val dependencyGoal = getGoal(entity, dependency.type)

                dependencyGoal.reset(dependency.input, goal.referrer)

                if (entity.hasComponent(ReferralComponent::class)) {
                    entity.logger.info("${entity[ReferralComponent::class].name} starting dependency goal ${dependency.type}")
                }

                dependencyGoal.start(dependency.input)

                if (dependencyGoal.failed) {
                    dependency.complete(false)
                } else {
                    goal.activeDependency = dependency
                }
            }
        } else {
            val dependencyGoal = getGoal(entity, dependency.type)
            if (updateDependencies(entity, dependencyGoal) && (dependencyGoal.fulfilled || dependencyGoal.failed)) {
                goal.activeDependency = null
                dependency.complete(!dependencyGoal.failed)
                return true
            }
        }
        return false
    }

    private fun executeGoal(entity: Entity, goal: Goal, input: GoalData, referrer: PlayerRequirement? = null) {
        goal.reset(input, referrer)

        entity[AIComponent::class].activeGoal = goal

        goal.start(input)
    }

    fun getGoal(entity: Entity, type: GoalType): Goal {
        return entity[AIComponent::class].goalInstances.computeIfAbsent(type) { type.create(entity) }
    }
}
