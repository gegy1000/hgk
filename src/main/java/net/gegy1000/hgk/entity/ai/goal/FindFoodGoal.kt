package net.gegy1000.hgk.entity.ai.goal

import net.gegy1000.hgk.arena.GroundType
import net.gegy1000.hgk.arena.Tile
import net.gegy1000.hgk.arena.VegetationType
import net.gegy1000.hgk.entity.Player

class FindFoodGoal(player: Player) : FindTileGoal(player, GoalType.FIND_FOOD) {
    override fun test(tile: Tile) = tile.groundType == GroundType.GROUND && (tile.vegetationType == VegetationType.SHRUBLAND || tile.vegetationType == VegetationType.FOREST)

    override fun found(x: Int, y: Int) {
        moveThen(x, y) { success ->
            if (success) {
                call(GoalType.HARVEST_FOOD) {
                    foundTile = it
                }
            }
        }
    }
}
