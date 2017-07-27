package net.gegy1000.hgk.entity.ai.goal

import net.gegy1000.hgk.arena.GroundType
import net.gegy1000.hgk.arena.tile.Tile
import net.gegy1000.hgk.arena.VegetationType
import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.EntityFamily
import net.gegy1000.hgk.entity.component.MetabolismComponent

class FindFoodGoal(entity: Entity) : FindTileGoal(entity, GoalType.FIND_FOOD) {
    override val family = EntityFamily.and(super.family, EntityFamily.all(MetabolismComponent::class))

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
