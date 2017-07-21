package net.gegy1000.hgk.arena

data class Tile(val x: Int, val y: Int, val height: Int, val temperature: Float, val groundType: GroundType) {
    fun toLong(): Long {
        val msb = java.lang.Float.floatToIntBits(temperature).toLong()
        val lsb = (height.toLong() shl 16) or groundType.ordinal.toLong()
        return (msb shl 32) or lsb
    }
}
