package net.gegy1000.hg.player.ai.goal

import net.gegy1000.hg.player.Player
import net.gegy1000.hg.player.ai.navigation.Path

class MoveToGoal(player: Player) : Goal(player, Goal.Type.MOVE_TO) {
    override val fulfilled: Boolean
        get() {
            return player.tileX == path?.targetX && player.tileY == path?.targetY
        }

    var path: Path? = null

    override fun start(input: GoalData) {
        val destinationX: Int = input["x"]
        val destinationY: Int = input["y"]
        path = player.navigator.findPath(destinationX, destinationY)
        if (path == null) {
            failed = true
        }
    }

    override fun update(input: GoalData) {
        call(Goal.Type.FOLLOW_PATH, GoalData {
            it["path"] = path ?: Path(player.tileX, player.tileY, emptyArray())
        }) { success -> failed = !success }
    }
}
