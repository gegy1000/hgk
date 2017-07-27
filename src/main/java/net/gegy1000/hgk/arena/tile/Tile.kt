package net.gegy1000.hgk.arena.tile

import net.gegy1000.hgk.arena.GroundType
import net.gegy1000.hgk.arena.VegetationType

data class Tile(val x: Int, val y: Int, val height: Int, val groundType: GroundType, val vegetationType: VegetationType, var data: TileData? = null) {
    fun toLong(): Long {
        val height = height.toLong() shl 24
        val cover = (vegetationType.ordinal.toLong() shl 20) or (groundType.ordinal.toLong() shl 12)
        val data = data?.toInt() ?: TileType.EMPTY.id
        return height or cover or data.toLong()
    }
}
