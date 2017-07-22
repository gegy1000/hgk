package net.gegy1000.hgk.entity.ai.goal

import net.gegy1000.hgk.entity.Player
import net.gegy1000.hgk.entity.ai.navigation.Path

class FollowPathGoal(player: Player) : Goal(player, GoalType.MOVE_TO) {
    override val fulfilled: Boolean
        get() {
            val input = input ?: return true
            val path: Path = input["path"]
            return player.tileX == path.targetX && player.tileY == path.targetY
        }

    var currentTicks = 0
    var totalTicks = 0

    override fun start(input: GoalData) {
        val path: Path = input["path"]
        currentTicks = 0
        totalTicks = (path.length / player.tilesPerTick).toInt()
        player.logger.info("${player.info.name} following path to ${path.targetX}, ${path.targetY}")
    }

    override fun update(input: GoalData) {
        if (totalTicks > 0) {
            val path: Path = input["path"]
            val nodeIndex = Math.ceil(Math.min(++currentTicks / totalTicks.toDouble(), 1.0) * (path.length - 1)).toInt()
            val node = path[nodeIndex]
            player.tileX = node.x
            player.tileY = node.y
            player.ai.moved = true
            if (currentTicks >= totalTicks) {
                player.logger.info("${player.info.name} successfully followed path to ${path.targetX}, ${path.targetY}")
            }
        }
    }
}
