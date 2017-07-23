package net.gegy1000.hgk.arena

enum class GroundType(val cost: Int = 0, val walkable: Boolean = true) {
    GROUND,
    WATER(cost = 5),
    OUTSIDE(walkable = false),
    CORNUCOPIA
}
