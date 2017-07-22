package net.gegy1000.hgk.arena.generation.layer

import net.gegy1000.hgk.arena.VegetationType

class SeedVegetationLayer(seed: Long) : GenerationLayer(seed) {
    override fun generate(x: Int, y: Int, width: Int, height: Int): IntArray {
        val vegetationCount = VegetationType.values().size
        return seed(x, y, width, height) { localX, localY -> nextInt(vegetationCount) }
    }
}
