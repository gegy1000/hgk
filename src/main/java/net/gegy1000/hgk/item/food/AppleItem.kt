package net.gegy1000.hgk.item.food

import net.gegy1000.hgk.MetabolismConstants
import net.gegy1000.hgk.item.Item
import net.gegy1000.hgk.item.ItemType

class AppleItem(count: Int) : Item(ItemType.APPLE, count), FoodItem {
    override val weight = 0.07F

    override val foodAmount = MetabolismConstants.LARGE_FRUIT_FOOD_INCREMENT
}
