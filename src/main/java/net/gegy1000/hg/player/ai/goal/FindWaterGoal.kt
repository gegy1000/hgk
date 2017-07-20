package net.gegy1000.hg.player.ai.goal

import net.gegy1000.hg.HGSimulator
import net.gegy1000.hg.arena.GroundType
import net.gegy1000.hg.arena.Tile
import net.gegy1000.hg.player.Player

class FindWaterGoal(player: Player) : FindTileGoal(player, Goal.Type.FIND_WATER) {
    override fun test(tile: Tile) = tile.groundType == GroundType.WATER

    override fun found(x: Int, y: Int) {
        HGSimulator.LOGGER.info("${player.name} found water at $x, $y")
        moveThen(x, y) { success ->
            if (success) {
                player.waterProcessing += (player.maxHydration / 1.5).toInt()
            }
            foundTile = success
        }
    }
}
