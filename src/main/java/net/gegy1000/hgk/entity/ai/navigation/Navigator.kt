package net.gegy1000.hgk.entity.ai.navigation

import net.gegy1000.hgk.arena.Arena
import net.gegy1000.hgk.arena.GroundType
import net.gegy1000.hgk.entity.Player

class Navigator(private val player: Player) {
    companion object {
        const val STRAIGHT_COST = 10
        const val DIAGONAL_COST = 14
    }

    fun findPath(x: Int, y: Int): Path? {
        if (player.arena[x, y].groundType != GroundType.OUTSIDE) {
            val access = NodeAccess(player.arena)

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
        if (deltaX > deltaY) {
            return (DIAGONAL_COST * deltaY + STRAIGHT_COST * (deltaX - deltaY))
        } else {
            return (DIAGONAL_COST * deltaX + STRAIGHT_COST * (deltaY - deltaX))
        }
    }

    private class NodeAccess(private val arena: Arena) {
        private val nodes = Array<PathNode?>(Arena.SIZE * Arena.SIZE) { null }

        operator fun get(x: Int, y: Int): PathNode {
            if (x in Arena.COORD_RANGE && y in Arena.COORD_RANGE) {
                val index = x + y * Arena.SIZE
                return nodes[index] ?: createNode(index, x, y)
            } else {
                return PathNode(arena, x, y)
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
            val node = PathNode(arena, x, y)
            nodes[index] = node
            return node
        }
    }
}
