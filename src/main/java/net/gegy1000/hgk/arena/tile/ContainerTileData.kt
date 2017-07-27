package net.gegy1000.hgk.arena.tile

import net.gegy1000.hgk.item.Item

class ContainerTileData(vararg items: Item) : TileData(TileType.CONTAINER) {
    val items = arrayListOf(items)
}
