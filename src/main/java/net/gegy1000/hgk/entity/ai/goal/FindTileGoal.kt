package net.gegy1000.hgk.entity.ai.goal

import net.gegy1000.hgk.arena.Arena
import net.gegy1000.hgk.arena.Tile
import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.component.PositionComponent

abstract class FindTileGoal(entity: Entity, type: GoalType) : Goal(entity, type) {
    override val fulfilled: Boolean
        get() = foundTile

    var range: Int = 0
    var foundTile: Boolean = false

    override fun start(input: GoalData) {
        foundTile = false
        range = 0
    }

    override fun update(input: GoalData) {
        if (range >= Arena.SIZE / 2) {
            failed = true
            return
        }
        val position = entity[PositionComponent::class]
        if (test(entity.arena[position.tileX, position.tileY])) {
            found(position.tileX, position.tileY)
            return
        }
        var tries = 0
        while (tries++ < 10 && !foundTile) {
            val minY = position.tileY - range
            val maxY = position.tileY + range
            val minX = position.tileX - range
            val maxX = position.tileX + range
            val foundTiles = ArrayList<Tile>()
            for (localY in minY..maxY) {
                for (localX in minX..maxX) {
                    if (localX == minX || localX == maxX || localY == minY || localY == maxY) {
                        val tile = entity.arena[localX, localY]
                        if (test(tile)) {
                            foundTiles.add(tile)
                        }
                    }
                }
            }
            if (foundTiles.isNotEmpty()) {
                val tile = foundTiles[entity.random.nextInt(foundTiles.size)]
                found(tile.x, tile.y)
                range++
                return
            }
            range++
        }
    }

    protected fun moveThen(x: Int, y: Int, task: (success: Boolean) -> Unit) {
        val position = entity[PositionComponent::class]
        if (position.tileX == x && position.tileY == y) {
            task(true)
        } else {
            val data = GoalData {
                it["x"] = x
                it["y"] = y
            }
            call(GoalType.FOLLOW_PATH, data, task)
        }
    }

    protected abstract fun found(x: Int, y: Int)

    protected abstract fun test(tile: Tile): Boolean
}
