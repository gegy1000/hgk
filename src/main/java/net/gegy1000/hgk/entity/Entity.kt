package net.gegy1000.hgk.entity

import net.gegy1000.hgk.arena.Arena
import net.gegy1000.hgk.entity.component.EntityComponent
import net.gegy1000.hgk.entity.system.EntitySystem
import net.gegy1000.hgk.session.StatusMessage
import net.gegy1000.hgk.session.StatusMessage.Property
import org.slf4j.Logger
import java.util.Random
import kotlin.reflect.KClass

class Entity(val arena: Arena, val components: Set<EntityComponent>, val familySystems: List<EntitySystem>) {
    val random: Random
        get() = arena.session.random
    val logger: Logger
        get() = arena.session.logger

    var updateIndex: Int = 0

    operator fun <T : EntityComponent> get(type: KClass<T>): T {
        return getOrNull(type) ?: throw IllegalArgumentException("Required component $type does not exist on requested entity")
    }

    fun <T : EntityComponent> getOrNull(type: KClass<T>): T? = components.filterIsInstance(type.java).firstOrNull()

    fun hasComponent(type: KClass<out EntityComponent>) = components.filterIsInstance(type.java).isNotEmpty()

    fun post(key: String) = post(arrayOf(key))

    fun post(keys: Array<String>, properties: Array<Property> = emptyArray()) {
        val message = StatusMessage(keys, properties + StatusMessage.getDefaultProperties(this)) { it[random.nextInt(it.size)] }
        arena.session.post(message)
    }
}
