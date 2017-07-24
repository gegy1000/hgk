package net.gegy1000.hgk.entity.system

import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.EntityFamily
import net.gegy1000.hgk.entity.component.PositionComponent
import net.gegy1000.hgk.entity.system.EntitySystem.Phase

class PositionSystem : EntitySystem {
    override val phase: Phase
        get() = Phase.PRE_TICK

    override val family = EntityFamily.all(PositionComponent::class)

    override fun update(entity: Entity) {
        val position = entity[PositionComponent::class]
        position.lastX = position.x
        position.lastY = position.y
    }
}
