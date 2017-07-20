package net.gegy1000.hg.player.ai.goal

import net.gegy1000.hg.HGSimulator
import net.gegy1000.hg.arena.Arena
import net.gegy1000.hg.arena.GroundType
import net.gegy1000.hg.player.Player

class HarvestFoodGoal(player: Player) : Goal(player, Goal.Type.HARVEST_FOOD) {
    override val fulfilled: Boolean
        get() = true

    override fun start(input: GoalData) {
    }

    override fun update(input: GoalData) {
        val tile = Arena[player.tileX, player.tileY]
        if (tile.groundType == GroundType.FOREST || tile.groundType == GroundType.BUSHES) {
            val poison = HGSimulator.random.nextFloat()
            if (player.statistics.plantKnowledge > poison / 4.0) {
                player.foodProcessing += 40
            } else {
                //TODO: rip you
                HGSimulator.LOGGER.info("${player.name} ate poisonous food")
            }
        }
    }
}
