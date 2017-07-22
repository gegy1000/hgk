package net.gegy1000.hgk.entity.ai.goal

import net.gegy1000.hgk.MetabolismConstants
import net.gegy1000.hgk.arena.GroundType
import net.gegy1000.hgk.arena.Tile
import net.gegy1000.hgk.entity.Player

class FindWaterGoal(player: Player) : FindTileGoal(player, GoalType.FIND_WATER) {
    override fun test(tile: Tile) = tile.groundType == GroundType.WATER

    override fun found(x: Int, y: Int) {
        moveThen(x, y) { success ->
            if (success) {
                player.metabolism.drinkWater(MetabolismConstants.DRINK_WATER_INCREMENT)
                player.post(arrayOf("goal.find_water.action", "goal.find_water.infinitive"))
            }
            foundTile = success
        }
    }
}
