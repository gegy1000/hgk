package net.gegy1000.hgk.entity.system

import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.component.EntityComponent
import kotlin.reflect.KClass

interface EntitySystem {
    val phase: Phase
        get() = Phase.TICK

    val dependencies: List<KClass<out EntityComponent>>

    fun update(entity: Entity)

    enum class Phase {
        PRE_TICK,
        TICK,
        POST_TICK
    }
}
