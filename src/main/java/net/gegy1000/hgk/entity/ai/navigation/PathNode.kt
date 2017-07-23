package net.gegy1000.hgk.entity.ai.navigation

import net.gegy1000.hgk.arena.Arena
import java.util.ArrayList

class PathNode(arena: Arena, val x: Int, val y: Int) {
    val tile = arena[x, y]

    var parent: PathNode? = null

    val walkable = tile.groundType.walkable

    var gCost: Int = 0
    var hCost: Int = 0
    val fCost: Int
        get() = gCost + hCost

    fun tracePath(): List<PathNode> {
        val path = ArrayList<PathNode>()
        parent?.let { path.addAll(it.tracePath()) }
        path.add(this)
        return path
    }

    override fun hashCode() = x shl 16 or y

    override fun equals(other: Any?) = other is PathNode && other.x == x && other.y == y
}
