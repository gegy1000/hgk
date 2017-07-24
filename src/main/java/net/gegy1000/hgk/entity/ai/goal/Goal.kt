package net.gegy1000.hgk.entity.ai.goal

import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.EntityFamily
import net.gegy1000.hgk.entity.ai.PlayerRequirement
import java.util.concurrent.LinkedBlockingDeque

abstract class Goal(val entity: Entity, val type: GoalType) {
    abstract val family: EntityFamily

    abstract val fulfilled: Boolean

    open var failed: Boolean = false

    var referrer: PlayerRequirement? = null
    var input: GoalData? = null

    val dependencyQueue = LinkedBlockingDeque<Dependency>()
    var activeDependency: Dependency? = null

    fun reset(input: GoalData, referrer: PlayerRequirement?) {
        this.activeDependency = null
        this.dependencyQueue.clear()
        this.input = input
        this.referrer = referrer
        this.failed = false
    }

    abstract fun start(input: GoalData)

    abstract fun update(input: GoalData)

    protected fun call(type: GoalType, input: GoalData = GoalData(), complete: (Boolean) -> Unit = {}) {
        dependencyQueue.add(Dependency(type, input, complete))
    }

    data class Dependency(val type: GoalType, val input: GoalData, val complete: (Boolean) -> Unit)
}
