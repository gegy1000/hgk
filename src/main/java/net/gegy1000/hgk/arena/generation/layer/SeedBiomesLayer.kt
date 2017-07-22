package net.gegy1000.hgk.arena.generation.layer

import net.gegy1000.hgk.arena.Biome

class SeedBiomesLayer(seed: Long) : GenerationLayer(seed) {
    override fun generate(x: Int, y: Int, width: Int, height: Int): IntArray {
        val biomeCount = Biome.values().size
        return seed(x, y, width, height) { localX, localY -> nextInt(biomeCount) }
    }
}
