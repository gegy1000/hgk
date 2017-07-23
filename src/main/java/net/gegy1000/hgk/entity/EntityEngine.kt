package net.gegy1000.hgk.entity

import net.gegy1000.hgk.arena.Arena
import net.gegy1000.hgk.entity.component.EntityComponent
import net.gegy1000.hgk.entity.system.AISystem
import net.gegy1000.hgk.entity.system.EntitySystem
import net.gegy1000.hgk.entity.system.InfluenceMapSystem
import net.gegy1000.hgk.entity.system.MetabolismSystem
import net.gegy1000.hgk.entity.system.NavigationSystem
import net.gegy1000.hgk.entity.system.PlayerMovementSystem
import net.gegy1000.hgk.entity.system.PositionSystem
import net.gegy1000.hgk.entity.system.SleepSystem

class EntityEngine(val arena: Arena) {
    val builder: Builder
        get() = Builder(this)

    val entities = ArrayList<Entity>()

    val systems = ArrayList<EntitySystem>()

    init {
        systems += PositionSystem()
        systems += InfluenceMapSystem()
        systems += SleepSystem()
        systems += MetabolismSystem()
        systems += NavigationSystem()
        systems += PlayerMovementSystem()
        systems += AISystem()
    }

    fun update() {
        entities.forEach {
            updatePhase(it, EntitySystem.Phase.PRE_TICK)
            updatePhase(it, EntitySystem.Phase.TICK)
            updatePhase(it, EntitySystem.Phase.POST_TICK)
        }
    }

    private fun updatePhase(entity: Entity, phase: EntitySystem.Phase) {
        entity.systems.filter { it.phase == phase }.forEach {
            it.update(entity)
        }
    }

    class Builder(val engine: EntityEngine) {
        val components: MutableSet<EntityComponent> = HashSet()

        fun withComponent(component: EntityComponent): Builder = apply { components += component }

        fun build(): Entity {
            val componentTypes = components.map { it::class }
            return Entity(engine.arena, components, engine.systems.filter { componentTypes.containsAll(it.dependencies) })
        }
    }
}
