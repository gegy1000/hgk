package net.gegy1000.hgk.arena

enum class GroundType(val cost: Int = 0, val speedModifier: Double = 1.0, val walkable: Boolean = true) {
    GROUND,
    WATER(cost = 14, speedModifier = 0.5),
    OUTSIDE(walkable = false),
    CORNUCOPIA(speedModifier = 1.5)
}
