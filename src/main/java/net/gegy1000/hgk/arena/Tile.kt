package net.gegy1000.hgk.arena

data class Tile(val x: Int, val y: Int, val height: Int, val groundType: GroundType, val vegetationType: VegetationType) {
    fun toInt(): Int {
        return (height shl 16) or ((vegetationType.ordinal shl 8) or groundType.ordinal)
    }
}
