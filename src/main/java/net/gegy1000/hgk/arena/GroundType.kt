package net.gegy1000.hgk.arena

import net.gegy1000.hgk.entity.ai.navigation.Navigator

enum class GroundType(val cost: Int = 0, val speedModifier: Double = 1.0, val walkable: Boolean = true) {
    GROUND,
    WATER(cost = Navigator.DIAGONAL_COST, speedModifier = 0.5),
    OUTSIDE(walkable = false),
    CORNUCOPIA(speedModifier = 1.5)
}
