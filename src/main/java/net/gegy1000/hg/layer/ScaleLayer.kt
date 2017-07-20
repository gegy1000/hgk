package net.gegy1000.hg.layer

class ScaleLayer(seed: Long, parent: GenerationLayer) : GenerationLayer(seed, parent) {
    override fun generate(x: Int, y: Int, width: Int, height: Int): FloatArray {
        val sampleX = x shr 1
        val sampleY = y shr 1
        val sampleWidth = (width shr 1) + 2
        val sampleHeight = (height shr 1) + 2
        val sampled = sampleParent(sampleX, sampleY, sampleWidth, sampleHeight)

        val zoomWidth = sampleWidth - 1 shl 1
        val zoomHeight = sampleHeight - 1 shl 1
        val zoomData = FloatArray(zoomWidth * zoomHeight)

        for (localY in 0..sampleHeight - 2) {
            var index = (localY shl 1) * zoomWidth
            var localX = 0
            var topLeft = sampled[localX + 0 + (localY + 0) * sampleWidth]
            var bottomLeft = sampled[localX + 0 + (localY + 1) * sampleWidth]

            while (localX < sampleWidth - 1) {
                this.initSeed((localX + sampleX shl 1), (localY + sampleY shl 1))
                val topRight = sampled[localX + 1 + (localY + 0) * sampleWidth]
                val bottomRight = sampled[localX + 1 + (localY + 1) * sampleWidth]
                zoomData[index] = topLeft
                zoomData[index++ + zoomWidth] = average(topLeft, bottomLeft)
                zoomData[index] = average(topLeft, topRight)
                zoomData[index++ + zoomWidth] = average(topLeft, topRight, bottomLeft, bottomRight)
                topLeft = topRight
                bottomLeft = bottomRight
                localX++
            }
        }

        val result = FloatArray(width * height)

        repeat(height) { localY ->
            System.arraycopy(zoomData, (localY + (y and 1)) * zoomWidth + (x and 1), result, localY * width, width)
        }

        return result
    }

    private inline fun average(vararg values: Float): Float {
        return values.average().toFloat()
    }
}
