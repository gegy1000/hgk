package net.gegy1000.hgk.arena.generation.layer

import net.gegy1000.hgk.arena.Biome

class CreatePathLayer(seed: Long, val parent: GenerationLayer) : GenerationLayer(seed) {
    override fun generate(x: Int, y: Int, width: Int, height: Int): IntArray {
        val sampled = parent.generate(x, y, width, height)
        val result = IntArray(width * height)

        val offsetX = width / 2
        val offsetY = height / 2

        repeat(height) { localY ->
            repeat(width) { localX ->
                val index = localX + localY * width
                val deltaX = Math.abs(localX - offsetX)
                val deltaY = Math.abs(localY - offsetY)
                if (Math.abs(deltaX - deltaY) < 2) {
                    result[index] = Biome.FLAT.ordinal
                } else {
                    result[index] = sampled[index]
                }
            }
        }

        return result
    }
}
