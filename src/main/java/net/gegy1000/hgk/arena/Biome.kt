package net.gegy1000.hgk.arena

enum class Biome(val min: Int, val max: Int) {
    WATER(0, 30),
    FLAT(32, 42),
    HILL(50, 60),
    MOUNTAINS(65, 80);

    val range = max - min
}
