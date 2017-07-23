package net.gegy1000.hgk.entity

import net.gegy1000.hgk.arena.Arena
import net.gegy1000.hgk.entity.ai.navigation.NodeInfluenceMap
import org.slf4j.Logger

abstract class Entity(val arena: Arena, var x: Double, var y: Double) {
    abstract val type: String

    abstract val influenceMap: NodeInfluenceMap

    open val influenceRange: Int = 1

    open val model: Any?
        get() = null

    val logger: Logger
        get() = arena.session.logger

    var tickIndex = 0

    val tileX: Int
        get() = x.toInt()

    val tileY: Int
        get() = y.toInt()

    var lastX: Double = x
    var lastY: Double = y

    open fun update() {
        lastX = x
        lastY = y

        tickIndex++
    }

    open fun getInfluence(entity: Entity): Int = 0

    open fun distance(x: Double, y: Double): Double {
        val deltaX = this.x - x
        val deltaY = this.y - y
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY)
    }

    open fun distance(entity: Entity) = distance(entity.x, entity.y)
}
