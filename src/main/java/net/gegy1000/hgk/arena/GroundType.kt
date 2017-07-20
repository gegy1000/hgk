package net.gegy1000.hgk.arena

import javafx.scene.paint.Color

enum class GroundType(val colour: Color) {
    FOREST(Color.rgb(0x51, 0x9B, 0x41, 1.0)),
    BUSHES(Color.rgb(0x97, 0xE5, 0x8E, 1.0)),
    OPEN(Color.rgb(0xDD, 0xE0, 0x46, 1.0)),
    WATER(Color.rgb(0, 0, 0xFF, 1.0)),
    OUTSIDE(Color.rgb(0xAA, 0xBB, 0xFF, 1.0)),
    CORNUCOPIA(Color.rgb(0xFF, 0xFF, 0, 1.0))
}
