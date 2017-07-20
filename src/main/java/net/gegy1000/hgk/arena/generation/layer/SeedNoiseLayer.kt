package net.gegy1000.hgk.arena.generation.layer

class SeedNoiseLayer(private val min: Float, private val max: Float, seed: Long) : GenerationLayer<Float>(seed) {
    private val range: Float = max - min

    override fun generate(x: Int, y: Int, width: Int, height: Int): Array<Float> {
        val result = Array(width * height) { 0.0F }
        repeat(height) { localY ->
            repeat(width) { localX ->
                initSeed(localX, localY)
                val index = localX + localY * width
                result[index] = nextFloat() * range + min
            }
        }
        return result
    }
}
