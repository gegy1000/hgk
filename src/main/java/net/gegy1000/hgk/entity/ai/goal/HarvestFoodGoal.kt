package net.gegy1000.hgk.entity.ai.goal

import net.gegy1000.hgk.arena.GroundType
import net.gegy1000.hgk.entity.Player

class HarvestFoodGoal(player: Player) : Goal(player, Goal.Type.HARVEST_FOOD) {
    override val fulfilled: Boolean
        get() = true

    override fun start(input: GoalData) {
    }

    override fun update(input: GoalData) {
        val tile = player.arena[player.tileX, player.tileY]
        if (tile.groundType == GroundType.FOREST || tile.groundType == GroundType.BUSHES) {
            val poison = player.arena.session.random.nextFloat()
            if (player.statistics.plantKnowledge > poison / 4.0) {
                player.foodProcessing += 40
            } else {
                //TODO: rip you
                player.logger.info("${player.name} ate poisonous food")
            }
        }
    }
}
