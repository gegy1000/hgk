package net.gegy1000.hgk.item

import net.gegy1000.hgk.item.food.AppleItem
import net.gegy1000.hgk.item.food.BerriesItem

enum class ItemType(val id: Int, val create: (count: Int) -> Item) {
    APPLE(0, ::AppleItem),
    BERRIES(1, ::BerriesItem)
}
