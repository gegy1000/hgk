package net.gegy1000.hgk.entity.ai

import net.gegy1000.hgk.MetabolismConstants
import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.ai.goal.GoalData
import net.gegy1000.hgk.entity.ai.goal.GoalType
import net.gegy1000.hgk.entity.component.MetabolismComponent

enum class PlayerRequirement(val goal: GoalType, val baseWeight: Float, val minThreshold: Float = -1.0F, val weightProvider: (Entity) -> Float, val createInput: () -> GoalData = { GoalData() }) {
    FIND_FOOD(GoalType.FIND_FOOD, 1.0F, minThreshold = 0.1F, weightProvider = { it[MetabolismComponent::class].hunger }), // Hungry, not recently in combat
    FIND_WATER(GoalType.FIND_WATER, 1.0F, minThreshold = 0.1F, weightProvider = { it[MetabolismComponent::class].thirst }), // Thirst, not recently in combat
    REST(GoalType.REST, 0.8F, minThreshold = 0.5F, weightProvider = { 1.0F - (it[MetabolismComponent::class].stamina / MetabolismConstants.MAX_STAMINA.toFloat()) })
//    SHELTER(0.5F, weightProvider = { 1.0F }), // Cold, raining, night
//    MURDER(0.5F, weightProvider = { it.statistics.aggresivity }), // Not hungry
//    DEFEND(1.0F, weightProvider = { it.statistics.aggresivity * it.statistics.strength }), // Being attacked, enemies nearby
//    HIDE(1.0F, weightProvider = { (1.0F - it.statistics.aggresivity) / it.statistics.strength * it.statistics.speed }); // Being attacked, enemies nearby
}
