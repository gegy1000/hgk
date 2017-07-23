package net.gegy1000.hgk.entity.system

import net.gegy1000.hgk.MetabolismConstants
import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.component.LivingComponent
import net.gegy1000.hgk.entity.component.MetabolismComponent
import net.gegy1000.hgk.entity.component.MovementComponent
import net.gegy1000.hgk.entity.component.SleepComponent
import net.gegy1000.hgk.notNull

class MetabolismSystem : EntitySystem {
    override val dependencies = listOf(MetabolismComponent::class, LivingComponent::class)

    override fun update(entity: Entity) {
        val living = entity[LivingComponent::class]

        if (!living.dead) {
            val metabolism = entity[MetabolismComponent::class]

            val movement = entity.getOrNull(MovementComponent::class)
            val sleep = entity.getOrNull(SleepComponent::class)

            val processedEnergy = process(metabolism.energy, metabolism.foodProcessing, MetabolismConstants.MAX_FOOD, MetabolismConstants.FOOD_PROCESS_SPEED)
            metabolism.energy += processedEnergy
            metabolism.foodProcessing -= processedEnergy

            val processedWater = process(metabolism.hydration, metabolism.waterProcessing, MetabolismConstants.MAX_WATER, MetabolismConstants.WATER_PROCESS_SPEED)
            metabolism.hydration += processedWater
            metabolism.waterProcessing -= processedWater

            val staminaIncrement = if (movement?.movedTick.notNull()) MetabolismConstants.MOVING_STAMINA_DECREMENT else MetabolismConstants.IDLE_STAMINA_INCREMENT
            metabolism.stamina += staminaIncrement

            if (sleep?.sleepTime ?: 0 <= 0) {
                val energyDecrement = if (movement?.movedTick.notNull()) MetabolismConstants.MOVING_ENERGY_USAGE else MetabolismConstants.IDLE_ENERGY_USAGE
                val waterDecrement = if (movement?.movedTick.notNull()) MetabolismConstants.MOVING_WATER_USAGE else MetabolismConstants.IDLE_WATER_USAGE

                metabolism.energy -= energyDecrement
                metabolism.hydration -= waterDecrement

                if (metabolism.energy < 0) {
                    metabolism.energy = 0

                    if (metabolism.foodProcessing > 0) {
                        sleep?.sleepTime = metabolism.foodProcessing / MetabolismConstants.FOOD_PROCESS_SPEED

                        metabolism.energy = metabolism.foodProcessing * 2
                        metabolism.foodProcessing = 0

                        entity.post("event.no_energy")
                    } else {
                        living.dead = true
                        entity.post("event.starve")
                    }
                }

                if (metabolism.hydration < 0) {
                    metabolism.hydration = 0

                    living.dead = true
                    entity.post("event.dehydration")
                }

                if (metabolism.stamina < 0) {
                    // Rest your head, it's time for bed.
                    metabolism.stamina = 0

                    sleep?.sleepTime = MetabolismConstants.MAX_STAMINA / MetabolismConstants.IDLE_STAMINA_INCREMENT / 3

                    entity.post("event.no_energy")
                } else if (metabolism.stamina > MetabolismConstants.MAX_STAMINA) {
                    metabolism.stamina = MetabolismConstants.MAX_STAMINA
                }
            }
        }
    }

    private fun process(amount: Int, processing: Int, max: Int, speed: Int): Int {
        if (amount + speed <= max && processing >= speed) {
            return speed
        }
        return 0
    }
}
