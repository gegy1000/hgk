package net.gegy1000.hg.player.ai.goal

import net.gegy1000.hg.player.Player

class RestGoal(player: Player) : Goal(player, Goal.Type.REST) {
    override val fulfilled: Boolean
        get() = player.maxTiredness - player.tiredness > player.maxTiredness * 0.1

    override fun start(input: GoalData) {
    }

    override fun update(input: GoalData) {
        player.tiredness--
    }
}
