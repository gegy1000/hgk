package net.gegy1000.hgk.entity.ai.requirement

import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.EntityFamily
import net.gegy1000.hgk.entity.ai.goal.GoalType
import net.gegy1000.hgk.entity.component.MetabolismComponent
import net.gegy1000.hgk.entity.component.PlayerComponent
import net.gegy1000.hgk.entity.component.PositionComponent

object FindWaterRequirement : PlayerRequirement {
    override val family = EntityFamily.all(PlayerComponent::class, MetabolismComponent::class, PositionComponent::class)

    override val weightMultiplier = 1.0F
    override val weightThreshold = 0.1F

    override fun weight(entity: Entity) = entity[MetabolismComponent::class].thirst

    override fun goal(entity: Entity) = GoalType.FIND_WATER
}
