package net.gegy1000.hgk.entity.ai.goal

import net.gegy1000.hgk.MetabolismConstants
import net.gegy1000.hgk.arena.GroundType
import net.gegy1000.hgk.arena.Tile
import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.EntityFamily
import net.gegy1000.hgk.entity.component.MetabolismComponent

class FindWaterGoal(entity: Entity) : FindTileGoal(entity, GoalType.FIND_WATER) {
    override val family = EntityFamily.and(super.family, EntityFamily.all(MetabolismComponent::class))

    override fun test(tile: Tile) = tile.groundType == GroundType.WATER

    override fun found(x: Int, y: Int) {
        moveThen(x, y) { success ->
            if (success) {
                entity[MetabolismComponent::class].waterProcessing += MetabolismConstants.DRINK_WATER_INCREMENT
                entity.post(arrayOf("goal.find_water.action", "goal.find_water.infinitive"))
            }
            foundTile = success
        }
    }
}
