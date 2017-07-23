package net.gegy1000.hgk.entity.system

import net.gegy1000.hgk.MetabolismConstants
import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.component.MetabolismComponent
import net.gegy1000.hgk.entity.component.MovementComponent
import net.gegy1000.hgk.entity.component.NavigationComponent
import net.gegy1000.hgk.entity.component.PlayerComponent
import net.gegy1000.hgk.entity.component.PositionComponent

class PlayerMovementSystem : MovementSystem() {
    override val dependencies = listOf(PositionComponent::class, MetabolismComponent::class, PlayerComponent::class, MovementComponent::class, NavigationComponent::class)

    override fun getMoveSpeed(entity: Entity): Float {
        val metabolism = entity[MetabolismComponent::class]
        val player = entity[PlayerComponent::class]
        return player.speed * ((metabolism.stamina / MetabolismConstants.MAX_STAMINA.toFloat()) * 0.5F + 0.5F)
    }
}
