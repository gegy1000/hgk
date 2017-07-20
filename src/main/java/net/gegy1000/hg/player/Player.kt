package net.gegy1000.hg.player

import net.gegy1000.hg.Constants
import net.gegy1000.hg.HGSimulator
import net.gegy1000.hg.arena.Arena
import net.gegy1000.hg.player.ai.Requirement
import net.gegy1000.hg.player.ai.goal.Goal
import net.gegy1000.hg.player.ai.goal.Goal.Type
import net.gegy1000.hg.player.ai.goal.GoalData
import net.gegy1000.hg.player.ai.navigation.Navigator
import java.util.EnumMap

class Player(val name: String, val statistics: BasePlayerStatistics) : Entity(Arena.SIZE / 2, Arena.SIZE / 2) {
    val navigator = Navigator(this)

    val tilesPerTick: Float
        get() = statistics.speed * (1.0F - (bodyPartDamage[BodyPart.LEGS] ?: 0.0F)) * ((1.0F - (tiredness / maxTiredness)) * 0.5F + 0.5F)

    val maxHydration = Constants.TICKS_PER_DAY
    var hydration = maxHydration / 2

    val maxEnergy = Constants.TICKS_PER_DAY
    var energy = maxEnergy / 2

    val maxTiredness = 100
    var tiredness = 0

    val foodProcessingSpeed = 3
    var foodProcessing = 0

    val waterProcessSpeed = 5
    var waterProcessing = 0

    val thirst: Float
        get() = 1.0F - (hydration + waterProcessing * 1.4F) / maxHydration.toFloat()

    val hunger: Float
        get() = 1.0F - (energy + foodProcessing * 1.4F) / maxEnergy.toFloat()

    private var activeGoal: Goal? = null

    private val goalTypes: MutableMap<Type, Goal> = EnumMap(Type::class.java)

    private val bodyPartDamage: MutableMap<BodyPart, Float> = EnumMap(BodyPart::class.java)

    init {
        Goal.Type.values().forEach { goalTypes.put(it, it.create(this)) }
    }

    override fun update() {
        super.update()

        drainEnergy(1)

        if (waterProcessing > 0) {
            hydration += waterProcessSpeed
            waterProcessing -= waterProcessSpeed
        }

        if (foodProcessing > 0) {
            energy += foodProcessingSpeed
            foodProcessing -= foodProcessingSpeed
        }

        if (tiredness-- < 0) {
            tiredness = 0
        } else if (tiredness > maxTiredness) {
            tiredness = maxTiredness
        }

        if (hydration-- < 0) {
            // TODO: dumb player, that's for sure
        } else if (hydration > maxHydration) {
            hydration = maxHydration
        }

        updateRequirements()

        val goal = activeGoal

        if (goal != null) {
            if (goal.updateDependencies() && (goal.fulfilled || goal.failed)) {
                activeGoal = null
                if (!goal.failed) {
                    HGSimulator.LOGGER.info("$name completed goal ${goal.type}")
                } else {
                    HGSimulator.LOGGER.info("$name failed goal ${goal.type}")
                }
            }
        }
    }

    fun drainEnergy(amount: Int): Boolean {
        energy -= amount
        tiredness += amount
        if (energy < 0) {
            // TODO: Rest your head. It's time for bed.
            energy = 0
            return false
        }
        return true
    }

    private fun updateRequirements() {
        val requirements = Requirement.values().sortedByDescending {
            val weight = it.baseWeight * it.weightProvider(this)
            if (weight >= it.minThreshold) weight else -1.0F
        }
        requirements.firstOrNull()?.let { requirement ->
            if (activeGoal?.referrer != requirement) {
                if (requirement.baseWeight * requirement.weightProvider(this) >= requirement.minThreshold) {
                    HGSimulator.LOGGER.info("$name starting goal ${requirement.goal} from $requirement requirement")
                    executeGoal(getGoal(requirement.goal), requirement.createInput(), requirement)
                }
            }
        }
    }

    private fun executeGoal(goal: Goal, input: GoalData, referrer: Requirement? = null) {
        goal.activeDependency = null
        goal.dependencyQueue.clear()
        goal.input = input
        goal.referrer = referrer

        activeGoal = goal

        goal.start(input)
    }

    fun getGoal(type: Type): Goal = goalTypes[type] ?: throw IllegalStateException("could not find goal for $type")

    enum class BodyPart {
        HEAD,
        CHEST,
        ARMS,
        LEGS
    }
}
