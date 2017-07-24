package net.gegy1000.hgk.entity

import net.gegy1000.hgk.entity.component.EntityComponent
import net.gegy1000.hgk.typed
import kotlin.reflect.KClass

interface EntityFamily {
    fun supports(components: Set<EntityComponent>): Boolean

    companion object {
        fun all(vararg requirements: KClass<out EntityComponent>): EntityFamily {
            return object : EntityFamily {
                private val requirementTypes = requirements.toSet()

                override fun supports(components: Set<EntityComponent>) = components.typed().containsAll(requirementTypes)
            }
        }

        fun any(vararg requirements: KClass<out EntityComponent>): EntityFamily {
            return object : EntityFamily {
                private val requirementTypes = requirements.toSet()

                override fun supports(components: Set<EntityComponent>) = components.typed().any(requirementTypes::contains)
            }
        }

        fun none(vararg requirements: KClass<out EntityComponent>): EntityFamily {
            return object : EntityFamily {
                private val requirementTypes = requirements.toSet()

                override fun supports(components: Set<EntityComponent>) = !components.typed().any(requirementTypes::contains)
            }
        }

        fun and(vararg families: EntityFamily): EntityFamily {
            return object : EntityFamily {
                override fun supports(components: Set<EntityComponent>) = families.all { it.supports(components) }
            }
        }

        fun or(vararg families: EntityFamily): EntityFamily {
            return object : EntityFamily {
                override fun supports(components: Set<EntityComponent>) = families.any { it.supports(components) }
            }
        }
    }
}
