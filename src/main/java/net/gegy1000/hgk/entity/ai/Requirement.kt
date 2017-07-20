package net.gegy1000.hgk.entity.ai

import net.gegy1000.hgk.entity.Player
import net.gegy1000.hgk.entity.ai.goal.Goal
import net.gegy1000.hgk.entity.ai.goal.GoalData

enum class Requirement(val goal: Goal.Type, val baseWeight: Float, val minThreshold: Float = -1.0F, val weightProvider: (Player) -> Float, val createInput: () -> GoalData = { GoalData() }) {
    FIND_FOOD(Goal.Type.FIND_FOOD, 1.0F, minThreshold = 0.1F, weightProvider = { it.hunger }), // Hungry, not recently in combat
    FIND_WATER(Goal.Type.FIND_WATER, 1.0F, minThreshold = 0.1F, weightProvider = { it.thirst }), // Thirst, not recently in combat
    REST(Goal.Type.REST, 0.8F, minThreshold = 0.5F, weightProvider = { it.tiredness / it.maxTiredness.toFloat() })
//    SHELTER(0.5F, weightProvider = { 1.0F }), // Cold, raining, night
//    MURDER(0.5F, weightProvider = { it.statistics.aggresivity }), // Not hungry
//    DEFEND(1.0F, weightProvider = { it.statistics.aggresivity * it.statistics.strength }), // Being attacked, enemies nearby
//    HIDE(1.0F, weightProvider = { (1.0F - it.statistics.aggresivity) / it.statistics.strength * it.statistics.speed }); // Being attacked, enemies nearby
}
