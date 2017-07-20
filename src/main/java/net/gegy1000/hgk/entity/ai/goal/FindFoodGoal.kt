package net.gegy1000.hgk.entity.ai.goal

import net.gegy1000.hgk.arena.GroundType
import net.gegy1000.hgk.arena.Tile
import net.gegy1000.hgk.entity.Player

class FindFoodGoal(player: Player) : FindTileGoal(player, Goal.Type.FIND_FOOD) {
    override fun test(tile: Tile) = tile.groundType == GroundType.BUSHES || tile.groundType == GroundType.FOREST

    override fun found(x: Int, y: Int) {
        player.logger.info("${player.name} found food at $x, $y")
        moveThen(x, y) { success ->
            if (success) {
                call(Goal.Type.HARVEST_FOOD)
            }
            foundTile = success
        }
    }
}
