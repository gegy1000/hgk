package net.gegy1000.hgk.entity

import net.gegy1000.hgk.arena.Arena
import org.slf4j.Logger

abstract class Entity(val arena: Arena, x: Int, y: Int) {
    abstract val type: String

    open val model: Any?
        get() = null

    val logger: Logger
        get() = arena.session.logger

    var tileX: Int = x
    var tileY: Int = y

    open fun update() {
    }
}
