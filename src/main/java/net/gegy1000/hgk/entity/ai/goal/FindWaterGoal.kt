package net.gegy1000.hgk.entity.ai.goal

import net.gegy1000.hgk.arena.GroundType
import net.gegy1000.hgk.arena.Tile
import net.gegy1000.hgk.entity.Player

class FindWaterGoal(player: Player) : FindTileGoal(player, Goal.Type.FIND_WATER) {
    override fun test(tile: Tile) = tile.groundType == GroundType.WATER

    override fun found(x: Int, y: Int) {
        player.logger.info("${player.name} found water at $x, $y")
        moveThen(x, y) { success ->
            if (success) {
                player.waterProcessing += (player.maxHydration / 1.5).toInt()
            }
            foundTile = success
        }
    }
}
