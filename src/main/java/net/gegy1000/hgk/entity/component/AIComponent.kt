package net.gegy1000.hgk.entity.component

import net.gegy1000.hgk.entity.ai.goal.Goal
import net.gegy1000.hgk.entity.ai.goal.GoalType
import java.util.EnumMap

class AIComponent : EntityComponent {
    var activeGoal: Goal? = null

    val goalInstances = EnumMap<GoalType, Goal>(GoalType::class.java)
}
