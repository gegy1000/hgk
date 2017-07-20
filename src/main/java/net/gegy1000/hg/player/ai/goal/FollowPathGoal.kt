package net.gegy1000.hg.player.ai.goal

import net.gegy1000.hg.HGSimulator
import net.gegy1000.hg.player.Player
import net.gegy1000.hg.player.ai.navigation.Path

class FollowPathGoal(player: Player) : Goal(player, Goal.Type.MOVE_TO) {
    override val fulfilled: Boolean
        get() {
            val input = input ?: return true
            val path: Path = input["path"]
            return player.tileX == path.targetX && player.tileY == path.targetY
        }

    var currentTicks = 0
    var totalTicks = 0

    var lastNode = 0

    override fun start(input: GoalData) {
        val path: Path = input["path"]
        currentTicks = 0
        totalTicks = (path.length / player.tilesPerTick).toInt()
        lastNode = -1
        HGSimulator.LOGGER.info("${player.name} following path to ${path.targetX}, ${path.targetY}")
    }

    override fun update(input: GoalData) {
        if (totalTicks > 0) {
            val path: Path = input["path"]
            val nodeIndex = Math.ceil(Math.min(++currentTicks / totalTicks.toDouble(), 1.0) * (path.length - 1)).toInt()
            val node = path[nodeIndex]
            player.tileX = node.x
            player.tileY = node.y
            if (nodeIndex != lastNode) {
                if (!player.drainEnergy(2)) {
                    failed = true
                    return
                }
                lastNode = nodeIndex
            }
            if (currentTicks >= totalTicks) {
                HGSimulator.LOGGER.info("${player.name} successfully followed path to ${path.targetX}, ${path.targetY}")
            }
        }
    }
}
