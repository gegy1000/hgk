package net.gegy1000.hg.layer

class CreatePathLayer(private val pathHeight: Float, seed: Long, parent: GenerationLayer) : GenerationLayer(seed, parent) {
    override fun generate(x: Int, y: Int, width: Int, height: Int): FloatArray {
        val sampled = sampleParent(x, y, width, height)
        val result = FloatArray(width * height)

        val offsetX = width / 2
        val offsetY = height / 2

        repeat(height) { localY ->
            repeat(width) { localX ->
                val index = localX + localY * width
                val deltaX = Math.abs(localX - offsetX)
                val deltaY = Math.abs(localY - offsetY)
                if (Math.abs(deltaX - deltaY) < 2) {
                    result[index] = pathHeight
                } else {
                    result[index] = sampled[index]
                }
            }
        }

        return result
    }
}
