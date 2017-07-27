package net.gegy1000.hgk.arena.tile

abstract class TileData(val type: TileType) {
    fun toInt() = type.id and 0xFF
}
