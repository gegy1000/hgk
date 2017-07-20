package net.gegy1000.hg.player.ai.navigation

import net.gegy1000.hg.arena.Arena
import net.gegy1000.hg.arena.GroundType
import net.gegy1000.hg.player.Player

class Navigator(private val player: Player) {
    companion object {
        const val STRAIGHT_COST = 10
        const val DIAGONAL_COST = 14
    }

    fun findPath(x: Int, y: Int): Path? {
        if (Arena[x, y].groundType != GroundType.OUTSIDE) {
            val access = NodeAccess()

            val start = access[player.tileX, player.tileY]
            val target = access[x, y]

            val open = HashSet<PathNode>()
            val closed = HashSet<PathNode>()

            open.add(start)

            while (open.isNotEmpty()) {
                var current = open.first()
                open.forEach {
                    if (it.fCost < current.fCost || it.fCost == current.fCost && it.hCost < current.hCost) {
                        current = it
                    }
                }
                /*open.asSequence()
                        .filterIndexed { i, n -> i > 0 && (n.fCost < current.fCost || n.fCost == current.fCost && n.hCost < current.hCost) }
                        .forEach { current = it }*/

                open.remove(current)
                closed.add(current)

                if (current == target) {
                    break
                }

                for (neighbour in access.getNeighbours(current)) {
                    if (!neighbour.walkable || closed.contains(neighbour)) {
                        continue
                    }
                    val overallCost = current.gCost + getMovementCost(current, neighbour)
                    val hasNeighbour = open.contains(neighbour)
                    if (overallCost < neighbour.gCost || !hasNeighbour) {
                        neighbour.gCost = overallCost
                        neighbour.hCost = getMovementCost(neighbour, target)
                        neighbour.parent = current
                        if (!hasNeighbour) {
                            open.add(neighbour)
                        }
                    }
                }
            }

            val nodes = target.tracePath().toTypedArray()
            if (nodes[0] == start) {
                return Path(x, y, nodes)
            } else {
                return null
            }
        }
        return null
    }

    private fun getMovementCost(start: PathNode, target: PathNode): Int {
        val deltaX = Math.abs(start.x - target.x)
        val deltaY = Math.abs(start.y - target.y)
//        val deltaHeight = Math.abs(start.tile.height - target.tile.height)
        if (deltaX > deltaY) {
            return (DIAGONAL_COST * deltaY + STRAIGHT_COST * (deltaX - deltaY)) /*+ deltaHeight * STRAIGHT_COST*/
        } else {
            return (DIAGONAL_COST * deltaX + STRAIGHT_COST * (deltaY - deltaX)) /*+ deltaHeight * STRAIGHT_COST*/
        }
    }

    private class NodeAccess {
        private val nodes = Array<PathNode?>(Arena.SIZE * Arena.SIZE) { null }

        operator fun get(x: Int, y: Int): PathNode {
            if (x in 0..Arena.SIZE - 1 && y in 0..Arena.SIZE - 1) {
                val index = x + y * Arena.SIZE
                return nodes[index] ?: createNode(index, x, y)
            } else {
                return PathNode(x, y)
            }
        }

        fun getNeighbours(node: PathNode): List<PathNode> {
            val neighbours = ArrayList<PathNode>()
            for (offsetY in -1..1) {
                for (offsetX in -1..1) {
                    if (offsetX == 0 && offsetY == 0) {
                        continue
                    }
                    neighbours.add(this[node.x + offsetX, node.y + offsetY])
                }
            }
            return neighbours
        }

        private fun createNode(index: Int, x: Int, y: Int): PathNode {
            val node = PathNode(x, y)
            nodes[index] = node
            return node
        }
    }
}
