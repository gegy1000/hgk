package net.gegy1000.hgk.entity.system

import net.gegy1000.hgk.MetabolismConstants
import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.EntityFamily
import net.gegy1000.hgk.entity.component.MetabolismComponent
import net.gegy1000.hgk.entity.component.PlayerComponent

class PlayerMovementSystem : MovementSystem() {
    override val family = EntityFamily.and(super.family, EntityFamily.all(PlayerComponent::class, MetabolismComponent::class))

    override fun getMoveSpeed(entity: Entity): Float {
        val metabolism = entity[MetabolismComponent::class]
        val player = entity[PlayerComponent::class]
        return player.speed * ((metabolism.stamina / MetabolismConstants.MAX_STAMINA.toFloat()) * 0.5F + 0.5F)
    }
}
