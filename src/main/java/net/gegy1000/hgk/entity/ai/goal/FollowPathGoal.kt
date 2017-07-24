package net.gegy1000.hgk.entity.ai.goal

import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.EntityFamily
import net.gegy1000.hgk.entity.component.MovementComponent
import net.gegy1000.hgk.entity.component.NavigationComponent
import net.gegy1000.hgk.entity.component.PositionComponent

class FollowPathGoal(entity: Entity) : Goal(entity, GoalType.FOLLOW_PATH) {
    override val family = EntityFamily.all(PositionComponent::class, MovementComponent::class, NavigationComponent::class)

    override val fulfilled: Boolean
        get() {
            val input = input ?: return true
            val targetX: Int = input["x"]
            val targetY: Int = input["y"]
            val position = entity[PositionComponent::class]
            return position.tileX == targetX && position.tileY == targetY
        }

    override fun start(input: GoalData) {
        val targetX: Int = input["x"]
        val targetY: Int = input["y"]
        entity[NavigationComponent::class].target = NavigationComponent.Target(targetX, targetY)
    }

    override fun update(input: GoalData) {
    }
}
