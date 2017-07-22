package net.gegy1000.hgk.entity

import net.gegy1000.hgk.MetabolismConstants

class Metabolism(val player: Player) {
    val hunger: Float
        get() = 1.0F - (energy + foodProcessing * 0.8F) / MetabolismConstants.MAX_FOOD.toFloat()

    val thirst: Float
        get() = 1.0F - (hydration + waterProcessing * 0.8F) / MetabolismConstants.MAX_WATER.toFloat()

    // TODO: More realistic start values + noise
    var hydration = MetabolismConstants.MAX_WATER / 2/*(MetabolismConstants.MAX_WATER * 1.8).toInt()*/
    var energy = MetabolismConstants.MAX_FOOD / 2/*(MetabolismConstants.MAX_FOOD * 1.8).toInt()*/

    var stamina = MetabolismConstants.MAX_STAMINA

    private var foodProcessing = 0
    private var waterProcessing = 0

    fun update(moved: Boolean) {
        val processedEnergy = process(energy, foodProcessing, MetabolismConstants.MAX_FOOD, MetabolismConstants.FOOD_PROCESS_SPEED)
        energy += processedEnergy
        foodProcessing -= processedEnergy

        val processedWater = process(hydration, waterProcessing, MetabolismConstants.MAX_WATER, MetabolismConstants.WATER_PROCESS_SPEED)
        hydration += processedWater
        waterProcessing -= processedWater

        val staminaIncrement = if (moved) MetabolismConstants.MOVING_STAMINA_DECREMENT else MetabolismConstants.IDLE_STAMINA_INCREMENT
        stamina += staminaIncrement

        if (player.sleepTime <= 0) {
            val energyDecrement = if (moved) MetabolismConstants.MOVING_ENERGY_USAGE else MetabolismConstants.IDLE_ENERGY_USAGE
            val waterDecrement = if (moved) MetabolismConstants.MOVING_WATER_USAGE else MetabolismConstants.IDLE_WATER_USAGE

            energy -= energyDecrement
            hydration -= waterDecrement

            if (energy < 0) {
                energy = 0

                if (foodProcessing > 0) {
                    player.sleepTime = foodProcessing / MetabolismConstants.FOOD_PROCESS_SPEED

                    energy = foodProcessing * 2
                    foodProcessing = 0

                    player.post("event.no_energy")
                } else {
                    player.dead = true
                    player.post("event.starve")
                }
            }

            if (hydration < 0) {
                hydration = 0

                player.dead = true
                player.post("event.dehydration")
            }

            if (stamina < 0) {
                // Rest your head, it's time for bed.

                stamina = 0

                player.sleepTime = MetabolismConstants.MAX_STAMINA / MetabolismConstants.IDLE_STAMINA_INCREMENT / 3

                player.post("event.no_energy")
            } else if (stamina > MetabolismConstants.MAX_STAMINA) {
                stamina = MetabolismConstants.MAX_STAMINA
            }
        }
    }

    private fun process(amount: Int, processing: Int, max: Int, speed: Int): Int {
        if (amount + speed <= max && processing >= speed) {
            return speed
        }
        return 0
    }

    fun eatFood(amount: Int) {
        foodProcessing += amount
    }

    fun drinkWater(amount: Int) {
        waterProcessing += amount
    }
}
