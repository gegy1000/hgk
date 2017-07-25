package net.gegy1000.hgk.entity.ai.requirement

import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.EntityFamily
import net.gegy1000.hgk.entity.ai.goal.GoalData
import net.gegy1000.hgk.entity.ai.goal.GoalType

interface PlayerRequirement {
    val family: EntityFamily

    val weightMultiplier: Float
    val weightThreshold: Float

    fun weight(entity: Entity): Float

    fun goal(entity: Entity): GoalType

    fun data(entity: Entity, goalType: GoalType): GoalData = GoalData()
}
