package net.gegy1000.hgk.entity.ai.goal

import net.gegy1000.hgk.entity.Entity

enum class GoalType(val create: (entity: Entity) -> Goal) {
    FOLLOW_PATH(::FollowPathGoal),
    FIND_WATER(::FindWaterGoal),
    FIND_FOOD(::FindFoodGoal),
    HARVEST_FOOD(::HarvestFoodGoal),
    REST(::RestGoal)
}
