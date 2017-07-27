package net.gegy1000.hgk.item

abstract class Item(val type: ItemType, val count: Int) {
    abstract val weight: Float
}
