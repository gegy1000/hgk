package net.gegy1000.hg.player.ai.navigation

import net.gegy1000.hg.arena.Arena
import net.gegy1000.hg.arena.GroundType
import java.util.ArrayList

data class PathNode(val x: Int, val y: Int) {
    val tile = Arena[x, y]

    var parent: PathNode? = null

    val walkable = tile.groundType != GroundType.WATER && tile.groundType != GroundType.OUTSIDE

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
}
