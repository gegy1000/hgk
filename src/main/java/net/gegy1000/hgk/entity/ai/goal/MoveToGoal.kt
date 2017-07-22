package net.gegy1000.hgk.entity.ai.goal

import net.gegy1000.hgk.entity.Player
import net.gegy1000.hgk.entity.ai.navigation.Path

class MoveToGoal(player: Player) : Goal(player, GoalType.MOVE_TO) {
    override val fulfilled: Boolean
        get() {
            return player.tileX == path?.targetX && player.tileY == path?.targetY
        }

    var path: Path? = null

    override fun start(input: GoalData) {
        val destinationX: Int = input["x"]
        val destinationY: Int = input["y"]
        path = player.ai.navigator.findPath(destinationX, destinationY)
        if (path == null) {
            failed = true
        }
    }

    override fun update(input: GoalData) {
        call(GoalType.FOLLOW_PATH, GoalData {
            it["path"] = path ?: Path(player.tileX, player.tileY, emptyArray())
        }) { success -> failed = !success }
    }
}
