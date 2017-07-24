package net.gegy1000.hgk.entity.system

import net.gegy1000.hgk.TimerConstants
import net.gegy1000.hgk.arena.Arena
import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.EntityFamily
import net.gegy1000.hgk.entity.component.InfluenceComponent
import net.gegy1000.hgk.entity.component.InfluenceMapComponent
import net.gegy1000.hgk.entity.component.PositionComponent

class InfluenceMapSystem : EntitySystem {
    override val family = EntityFamily.all(InfluenceMapComponent::class)

    override fun update(entity: Entity) {
        if (entity.updateIndex % TimerConstants.PATH_RECALCULATE_TICKS == 0) {
            val influenceMap = entity[InfluenceMapComponent::class]
            val entities = entity.arena.session.entityEngine.entities.filter { entity != it && it.hasComponent(PositionComponent::class) && it.hasComponent(InfluenceComponent::class) }
            if (entities.isNotEmpty()) {
                for (localY in Arena.COORD_RANGE) {
                    for (localX in Arena.COORD_RANGE) {
                        influenceMap[localX, localY] = entities.sumByDouble {
                            val influence = it[InfluenceComponent::class]
                            val distance = it[PositionComponent::class].distance(localX.toDouble(), localY.toDouble()) / influence.range
                            influence.cost * Math.min(Math.max(1.0 - distance, 0.0), 1.0)
                        }.toShort()
                    }
                }
            }
        }
    }
}
