package net.gegy1000.hgk.entity.system

import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.EntityFamily

interface EntitySystem {
    val phase: Phase
        get() = Phase.TICK

    val family: EntityFamily

    fun update(entity: Entity)

    enum class Phase {
        PRE_TICK,
        TICK,
        POST_TICK
    }
}
