package net.gegy1000.hg.layer

class SeedNoiseLayer(private val min: Float, private val max: Float, seed: Long) : GenerationLayer(seed) {
    private val range: Float = max - min

    override fun generate(x: Int, y: Int, width: Int, height: Int): FloatArray {
        val result = FloatArray(width * height)
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
