package net.gegy1000.hgk.item.food

import net.gegy1000.hgk.MetabolismConstants
import net.gegy1000.hgk.item.Item
import net.gegy1000.hgk.item.ItemType

class BerriesItem(count: Int) : Item(ItemType.BERRIES, count), FoodItem {
    override val weight = 0.04F

    override val foodAmount = MetabolismConstants.SMALL_FRUIT_FOOD_INCREMENT
}
