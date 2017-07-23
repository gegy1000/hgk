package net.gegy1000.hgk.entity.ai.goal

import net.gegy1000.hgk.TimerConstants
import net.gegy1000.hgk.entity.Player
import net.gegy1000.hgk.entity.ai.navigation.Path

class FollowPathGoal(player: Player) : Goal(player, GoalType.FOLLOW_PATH) {
    override val fulfilled: Boolean
        get() {
            val input = input ?: return true
            val targetX: Int = input["x"]
            val targetY: Int = input["y"]
            return player.tileX == targetX && player.tileY == targetY
        }

    var path: Path? = null
    var currentNodeIndex = 0

    var lastRecalculate = 0

    override fun start(input: GoalData) {
        val targetX: Int = input["x"]
        val targetY: Int = input["y"]
        path = calculatePath(targetX, targetY)
    }

    override fun update(input: GoalData) {
        if (player.tickIndex - lastRecalculate >= TimerConstants.PATH_RECALCULATE_TICKS) {
            val targetX: Int = input["x"]
            val targetY: Int = input["y"]
            path = calculatePath(targetX, targetY)
        }

        val path = path ?: return
        if (path.length > 0 && currentNodeIndex < path.length) {
            val node = path[currentNodeIndex]
            val deltaX = (node.x + 0.5) - player.x
            val deltaY = (node.y + 0.5) - player.y
            val length = Math.sqrt(deltaX * deltaX + deltaY * deltaY) / player.tilesPerTick * node.tile.groundType.speedModifier

            player.x += deltaX / length
            player.y += deltaY / length

            player.ai.moved = true

            if (player.tileX == node.x && player.tileY == node.y) {
                currentNodeIndex++
            }
        }
    }

    private fun calculatePath(targetX: Int, targetY: Int): Path? {
        currentNodeIndex = 0
        lastRecalculate = player.tickIndex
        val path = player.ai.navigator.findPath(targetX, targetY)
        if (path == null) {
            failed = true
        }
        return path
    }
}
