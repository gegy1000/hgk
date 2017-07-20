package net.gegy1000.hg.player.ai.goal

import net.gegy1000.hg.arena.Arena
import net.gegy1000.hg.arena.Tile
import net.gegy1000.hg.player.Player

abstract class FindTileGoal(player: Player, type: Goal.Type) : Goal(player, type) {
    override val fulfilled: Boolean
        get() = foundTile

    var range: Int = 0
    var foundTile: Boolean = false

    override fun start(input: GoalData) {
        foundTile = false
        range = 0
    }

    override fun update(input: GoalData) {
        if (range >= Arena.SIZE) {
            failed = true
            return
        }
        if (test(Arena[player.tileX, player.tileY])) {
            found(player.tileX, player.tileY)
            return
        }
        var tries = 0
        while (tries++ < 10 && !foundTile) {
            val minY = player.tileY - range
            val maxY = player.tileY + range
            val minX = player.tileX - range
            val maxX = player.tileX + range
            for (localY in minY..maxY) {
                for (localX in minX..maxX) {
                    if (localX == minX || localX == maxX || localY == minY || localY == maxY) {
                        val tile = Arena[localX, localY]
                        if (test(tile)) {
                            found(localX, localY)
                            return
                        }
                    }
                }
            }
            range++
        }
    }

    protected fun moveThen(x: Int, y: Int, task: (success: Boolean) -> Unit) {
        if (player.tileX == x && player.tileY != y) {
            task(true)
        } else {
            val data = GoalData {
                it["x"] = x
                it["y"] = y
            }
            call(Goal.Type.MOVE_TO, data, task)
        }
    }

    protected abstract fun found(x: Int, y: Int)

    protected abstract fun test(tile: Tile): Boolean
}
