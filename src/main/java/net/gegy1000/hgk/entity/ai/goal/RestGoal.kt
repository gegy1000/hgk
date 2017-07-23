package net.gegy1000.hgk.entity.ai.goal

import net.gegy1000.hgk.MetabolismConstants
import net.gegy1000.hgk.TimerConstants
import net.gegy1000.hgk.entity.Player

class RestGoal(player: Player) : Goal(player, GoalType.REST) {
    override val fulfilled: Boolean
        get() = restTime > MetabolismConstants.MIN_REST_TIME && MetabolismConstants.MAX_STAMINA - player.metabolism.stamina > MetabolismConstants.MAX_STAMINA * 0.8

    var lastRest: Int = 0
    var restTime: Int = 0

    override fun start(input: GoalData) {
        val updateIndex = player.arena.session.updateIndex

        if (updateIndex - lastRest > TimerConstants.TICKS_PER_HOUR * 2) {
            player.post(arrayOf("goal.rest.action", "goal.rest.infinitive"))
        }

        lastRest = updateIndex
        restTime = 0
    }

    override fun update(input: GoalData) {
        restTime++
    }
}
