package net.gegy1000.hgk.entity.ai.goal

import net.gegy1000.hgk.MetabolismConstants
import net.gegy1000.hgk.arena.GroundType
import net.gegy1000.hgk.arena.VegetationType
import net.gegy1000.hgk.entity.Player
import net.gegy1000.hgk.session.StatusMessage.Property

class HarvestFoodGoal(player: Player) : Goal(player, GoalType.HARVEST_FOOD) {
    override val fulfilled: Boolean
        get() = true

    override fun start(input: GoalData) {
    }

    override fun update(input: GoalData) {
        val tile = player.arena[player.tileX, player.tileY]
        if (tile.groundType == GroundType.GROUND && (tile.vegetationType == VegetationType.SHRUBLAND || tile.vegetationType == VegetationType.FOREST)) {
            val foodType = if (player.random.nextFloat() > 0.5F) "berries" else "fruit"
            val properties = arrayOf(Property("food_type", foodType), Property("vegetation", tile.vegetationType.plantName))
            val poison = player.random.nextFloat()
            if (player.statistics.plantKnowledge > poison / 4.0) {
                player.metabolism.eatFood(MetabolismConstants.HARVEST_FOOD_INCREMENT)
                if (player.metabolism.hunger < 0.2F) {
                    player.post(arrayOf("goal.find_food_snack.action"), properties)
                } else {
                    player.post(arrayOf("goal.find_food.action", "goal.find_food.infinitive"), properties)
                }
            } else {
                player.post(arrayOf("event.poisoned_food"), properties)
                player.dead = true
            }
        }
    }
}
