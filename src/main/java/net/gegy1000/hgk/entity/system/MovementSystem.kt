package net.gegy1000.hgk.entity.system

import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.EntityFamily
import net.gegy1000.hgk.entity.ai.navigation.Path
import net.gegy1000.hgk.entity.component.LivingComponent
import net.gegy1000.hgk.entity.component.MovementComponent
import net.gegy1000.hgk.entity.component.NavigationComponent
import net.gegy1000.hgk.entity.component.PositionComponent
import net.gegy1000.hgk.notNull

abstract class MovementSystem : EntitySystem {
    override val family = EntityFamily.all(PositionComponent::class, MovementComponent::class, NavigationComponent::class)

    override fun update(entity: Entity) {
        val movement = entity[MovementComponent::class]
        movement.movedTick = false

        val living = entity.getOrNull(LivingComponent::class)
        if (!living?.dead.notNull()) {
            val navigationComponent = entity.getOrNull(NavigationComponent::class)
            navigationComponent?.currentPath?.let { followPath(entity, it) }
        }
    }

    abstract fun getMoveSpeed(entity: Entity): Float

    private fun followPath(entity: Entity, path: Path) {
        val position = entity[PositionComponent::class]
        val movementComponent = entity[MovementComponent::class]
        val navigationComponent = entity[NavigationComponent::class]

        if (path.targetX == position.tileX && path.targetY == position.tileY) {
            navigationComponent.currentPath = null
        } else if (path.length > 0 && path.currentNode < path.length) {
            val node = path[path.currentNode]
            val deltaX = (node.x + 0.5) - position.x
            val deltaY = (node.y + 0.5) - position.y
            val length = Math.sqrt(deltaX * deltaX + deltaY * deltaY) / getMoveSpeed(entity) * node.tile.groundType.speedModifier

            position.x += deltaX / length
            position.y += deltaY / length

            movementComponent.movedTick = true

            if (position.tileX == node.x && position.tileY == node.y) {
                path.currentNode++
            }
        }
    }
}
