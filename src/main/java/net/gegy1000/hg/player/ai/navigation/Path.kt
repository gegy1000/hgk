package net.gegy1000.hg.player.ai.navigation

class Path(val targetX: Int, val targetY: Int, private val nodes: Array<PathNode>): Iterable<PathNode> {
    val length = nodes.size

    operator fun get(index: Int) = nodes[index]

    override fun iterator() = nodes.iterator()
}
