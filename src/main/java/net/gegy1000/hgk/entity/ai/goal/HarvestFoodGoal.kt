package net.gegy1000.hgk.entity.ai.goal

import net.gegy1000.hgk.MetabolismConstants
import net.gegy1000.hgk.arena.GroundType
import net.gegy1000.hgk.arena.VegetationType
import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.entity.EntityFamily
import net.gegy1000.hgk.entity.component.LivingComponent
import net.gegy1000.hgk.entity.component.MetabolismComponent
import net.gegy1000.hgk.entity.component.PlayerComponent
import net.gegy1000.hgk.entity.component.PositionComponent
import net.gegy1000.hgk.session.StatusMessage.Property

class HarvestFoodGoal(entity: Entity) : Goal(entity, GoalType.HARVEST_FOOD) {
    override val family = EntityFamily.all(PositionComponent::class, PlayerComponent::class, MetabolismComponent::class)

    override val fulfilled: Boolean
        get() = true

    override fun start(input: GoalData) {
    }

    override fun update(input: GoalData) {
        val position = entity[PositionComponent::class]
        val tile = entity.arena[position.tileX, position.tileY]
        if (tile.groundType == GroundType.GROUND && (tile.vegetationType == VegetationType.SHRUBLAND || tile.vegetationType == VegetationType.FOREST)) {
            val foodType = if (entity.random.nextFloat() > 0.5F) "berries" else "fruit"
            val properties = arrayOf(Property("food_type", foodType), Property("vegetation", tile.vegetationType.plantName))
            val poison = entity.random.nextFloat()
            if (entity[PlayerComponent::class].plantKnowledge > poison / 4.0) {
                val metabolismComponent = entity[MetabolismComponent::class]
                metabolismComponent.foodProcessing += MetabolismConstants.LARGE_FRUIT_FOOD_INCREMENT
                if (metabolismComponent.hunger < 0.2F) {
                    entity.post(arrayOf("goal.find_food_snack.action"), properties)
                } else {
                    entity.post(arrayOf("goal.find_food.action", "goal.find_food.infinitive"), properties)
                }
            } else {
                entity.post(arrayOf("event.poisoned_food"), properties)
                entity[LivingComponent::class].dead = true
            }
        }
    }
}
