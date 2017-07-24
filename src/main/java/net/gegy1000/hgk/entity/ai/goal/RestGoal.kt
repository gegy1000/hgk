package net.gegy1000.hgk.entity.ai.goal

import net.gegy1000.hgk.MetabolismConstants
import net.gegy1000.hgk.TimerConstants
import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.EntityFamily
import net.gegy1000.hgk.entity.component.MetabolismComponent
import net.gegy1000.hgk.entity.component.PlayerComponent

class RestGoal(entity: Entity) : Goal(entity, GoalType.REST) {
    override val family = EntityFamily.all(PlayerComponent::class, MetabolismComponent::class)

    override val fulfilled: Boolean
        get() = restTime > MetabolismConstants.MIN_REST_TIME && MetabolismConstants.MAX_STAMINA - entity[MetabolismComponent::class].stamina > MetabolismConstants.MAX_STAMINA * 0.8

    var lastRest: Int = 0
    var restTime: Int = 0

    override fun start(input: GoalData) {
        val updateIndex = entity.arena.session.updateIndex

        if (updateIndex - lastRest > TimerConstants.TICKS_PER_HOUR * 2) {
            entity.post(arrayOf("goal.rest.action", "goal.rest.infinitive"))
        }

        lastRest = updateIndex
        restTime = 0
    }

    override fun update(input: GoalData) {
        restTime++
    }
}
