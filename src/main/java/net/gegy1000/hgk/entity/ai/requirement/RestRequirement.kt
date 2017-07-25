package net.gegy1000.hgk.entity.ai.requirement

import net.gegy1000.hgk.MetabolismConstants
import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.EntityFamily
import net.gegy1000.hgk.entity.ai.goal.GoalType
import net.gegy1000.hgk.entity.component.MetabolismComponent
import net.gegy1000.hgk.entity.component.PlayerComponent

object RestRequirement : PlayerRequirement {
    override val family = EntityFamily.all(PlayerComponent::class, MetabolismComponent::class)

    override val weightMultiplier = 0.8F
    override val weightThreshold = 0.5F

    override fun weight(entity: Entity) = 1.0F - (entity[MetabolismComponent::class].stamina / MetabolismConstants.MAX_STAMINA.toFloat())

    override fun goal(entity: Entity) = GoalType.REST
}
