package net.gegy1000.hgk.entity.system

import net.gegy1000.hgk.TimerConstants
import net.gegy1000.hgk.arena.Arena
import net.gegy1000.hgk.arena.GroundType
import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.EntityFamily
import net.gegy1000.hgk.entity.ai.navigation.Path
import net.gegy1000.hgk.entity.ai.navigation.PathNode
import net.gegy1000.hgk.entity.component.InfluenceMapComponent
import net.gegy1000.hgk.entity.component.LivingComponent
import net.gegy1000.hgk.entity.component.NavigationComponent
import net.gegy1000.hgk.entity.component.PositionComponent
import net.gegy1000.hgk.entity.system.EntitySystem.Phase

class NavigationSystem : EntitySystem {
    override val phase: Phase
        get() = Phase.PRE_TICK

    override val family = EntityFamily.all(NavigationComponent::class, PositionComponent::class, LivingComponent::class)

    override fun update(entity: Entity) {
        val living = entity[LivingComponent::class]
        if (!living.dead) {
            val navigation = entity[NavigationComponent::class]

            val targetChanged = navigation.target?.x != navigation.currentPath?.targetX && navigation.target?.y != navigation.currentPath?.targetY
            if (targetChanged || entity.updateIndex - navigation.lastPathRecalculation > TimerConstants.PATH_RECALCULATE_TICKS) {
                val target = navigation.target
                if (target != null) {
                    navigation.currentPath = findPath(entity, target.x, target.y)
                }
                navigation.lastPathRecalculation = entity.updateIndex
            }
        }
    }

    private fun findPath(entity: Entity, x: Int, y: Int): Path? {
        val position = entity[PositionComponent::class]
        if (entity.arena[x, y].groundType != GroundType.OUTSIDE) {
            val access = NodeAccess(entity.arena)

            val start = access[position.tileX, position.tileY]
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
                    val overallCost = current.gCost + getCost(entity, current, neighbour)
                    val hasNeighbour = open.contains(neighbour)
                    if (overallCost < neighbour.gCost || !hasNeighbour) {
                        neighbour.gCost = overallCost
                        neighbour.hCost = getHeuristic(entity, neighbour, target)
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

    private fun getCost(entity: Entity, start: PathNode, target: PathNode): Int {
        val influenceMap = entity.getOrNull(InfluenceMapComponent::class)
        val heuristicCost = getHeuristic(entity, start, target)
        val heightCost = Math.max(0, target.tile.height - start.tile.height) * entity[NavigationComponent::class].upCost
        val typeCost = target.tile.groundType.cost
        return heuristicCost + heightCost + typeCost + (influenceMap?.get(target.x, target.y) ?: 0)
    }

    private fun getHeuristic(entity: Entity, start: PathNode, target: PathNode): Int {
        val navigationComponent = entity[NavigationComponent::class]
        val deltaX = Math.abs(start.x - target.x)
        val deltaY = Math.abs(start.y - target.y)
        if (deltaX > deltaY) {
            return (navigationComponent.diagonalCost * deltaY + navigationComponent.straightCost * (deltaX - deltaY))
        } else {
            return (navigationComponent.diagonalCost * deltaX + navigationComponent.straightCost * (deltaY - deltaX))
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
                    if (offsetX != 0 || offsetY != 0) {
                        neighbours.add(this[node.x + offsetX, node.y + offsetY])
                    }
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
