package net.gegy1000.hgk.arena.generation.layer.scale

import net.gegy1000.hgk.arena.generation.layer.GenerationLayer

abstract class ScaleLayer(seed: Long, val parent: GenerationLayer) : GenerationLayer(seed) {
    override fun initParentSeed(seed: Long) = parent.initGlobalSeed(seed)

    override fun generate(x: Int, y: Int, width: Int, height: Int): IntArray {
        val sampleX = x shr 1
        val sampleY = y shr 1
        val sampleWidth = (width shr 1) + 2
        val sampleHeight = (height shr 1) + 2
        val sampled = parent.generate(sampleX, sampleY, sampleWidth, sampleHeight)

        val zoomWidth = sampleWidth - 1 shl 1
        val zoomHeight = sampleHeight - 1 shl 1
        val zoomData = IntArray(zoomWidth * zoomHeight)

        for (localY in 0..sampleHeight - 2) {
            var index = (localY shl 1) * zoomWidth
            var topLeft = sampled[localY * sampleWidth]
            var bottomLeft = sampled[(localY + 1) * sampleWidth]

            for (localX in 0..sampleWidth - 2) {
                this.initSeed(localX + sampleX shl 1, localY + sampleY shl 1)
                val topRight = sampled[localX + 1 + (localY + 0) * sampleWidth]
                val bottomRight = sampled[localX + 1 + (localY + 1) * sampleWidth]
                zoomData[index] = select(intArrayOf(topLeft))
                zoomData[zoomWidth + index++] = select(intArrayOf(topLeft, bottomLeft))
                zoomData[index] = select(intArrayOf(topLeft, topRight))
                zoomData[zoomWidth + index++] = select(intArrayOf(topLeft, topRight, bottomLeft, bottomRight))
                topLeft = topRight
                bottomLeft = bottomRight
            }
        }

        val result = IntArray(width * height)

        repeat(height) { localY ->
            System.arraycopy(zoomData, (localY + (y and 1)) * zoomWidth + (x and 1), result, localY * width, width)
        }

        return result
    }

    protected abstract fun select(values: IntArray): Int
}
