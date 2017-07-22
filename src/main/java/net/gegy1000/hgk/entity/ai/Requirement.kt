package net.gegy1000.hgk.entity.ai

import net.gegy1000.hgk.MetabolismConstants
import net.gegy1000.hgk.entity.Player
import net.gegy1000.hgk.entity.ai.goal.GoalData
import net.gegy1000.hgk.entity.ai.goal.GoalType

enum class Requirement(val goal: GoalType, val baseWeight: Float, val minThreshold: Float = -1.0F, val weightProvider: (Player) -> Float, val createInput: () -> GoalData = { GoalData() }) {
    FIND_FOOD(GoalType.FIND_FOOD, 1.0F, minThreshold = 0.1F, weightProvider = { it.metabolism.hunger }), // Hungry, not recently in combat
    FIND_WATER(GoalType.FIND_WATER, 1.0F, minThreshold = 0.1F, weightProvider = { it.metabolism.thirst }), // Thirst, not recently in combat
    REST(GoalType.REST, 0.8F, minThreshold = 0.5F, weightProvider = { 1.0F - (it.metabolism.stamina / MetabolismConstants.MAX_STAMINA.toFloat()) })
//    SHELTER(0.5F, weightProvider = { 1.0F }), // Cold, raining, night
//    MURDER(0.5F, weightProvider = { it.statistics.aggresivity }), // Not hungry
//    DEFEND(1.0F, weightProvider = { it.statistics.aggresivity * it.statistics.strength }), // Being attacked, enemies nearby
//    HIDE(1.0F, weightProvider = { (1.0F - it.statistics.aggresivity) / it.statistics.strength * it.statistics.speed }); // Being attacked, enemies nearby
}
