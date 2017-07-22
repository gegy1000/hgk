package net.gegy1000.hgk.arena.generation.layer

import net.gegy1000.hgk.arena.Biome

class GenerateHeightsLayer(private val radius: Int, seed: Long, val parent: GenerationLayer) : GenerationLayer(seed) {
    private val total = (radius * 2 + 1) * (radius * 2 + 1)

    override fun initParentSeed(seed: Long) {
        parent.initGlobalSeed(seed)
    }

    override fun generate(x: Int, y: Int, width: Int, height: Int): IntArray {
        val biomeTypes = Biome.values()
        val sampleX = x - radius
        val sampleY = y - radius
        val sampleWidth = width + radius * 2
        val sampleHeight = height + radius * 2
        val sampled = parent.generate(sampleX, sampleY, sampleWidth, sampleHeight)
        val result = IntArray(width * height)
        repeat(height) { localY ->
            repeat(width) { localX ->
                val sampleLocalX = localX + radius
                val sampleLocalY = localY + radius
                var value = 0
                for (bufferY in sampleLocalY - radius..sampleLocalY + radius) {
                    for (bufferX in sampleLocalX - radius..sampleLocalX + radius) {
                        initSeed(x + bufferX, y + bufferY)
                        val biome = biomeTypes[sampled[bufferX + bufferY * sampleWidth]]
                        value += nextInt(biome.range) + biome.min
                    }
                }
                result[localX + localY * width] = value / total
            }
        }
        return result
    }
}
