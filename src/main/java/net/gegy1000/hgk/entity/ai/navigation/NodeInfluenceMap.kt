package net.gegy1000.hgk.entity.ai.navigation

import net.gegy1000.hgk.arena.Arena
import net.gegy1000.hgk.entity.Entity

class NodeInfluenceMap(private val entity: Entity) {
    private val nodes = ShortArray(Arena.SIZE * Arena.SIZE)

    operator fun get(x: Int, y: Int) = nodes[x + y * Arena.SIZE].toInt()

    fun update() {
        val entities = entity.arena.session.entities
        val entityInfluence = entities.filterNot { entity == it }.map { it to it.getCostInfluence(entity) }
        if (entityInfluence.isNotEmpty()) {
            for (localY in Arena.COORD_RANGE) {
                for (localX in Arena.COORD_RANGE) {
                    nodes[localX + localY * Arena.SIZE] = entityInfluence.sumByDouble { (entity, influence) ->
                        val distance = entity.distance(localX.toDouble(), localY.toDouble()) / entity.influenceRange
                        influence * Math.min(Math.max(1.0 - distance, 0.0), 1.0)
                    }.toShort()
                }
            }
        }
    }
}
