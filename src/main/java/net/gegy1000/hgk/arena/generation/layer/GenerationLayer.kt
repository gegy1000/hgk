package net.gegy1000.hgk.arena.generation.layer

abstract class GenerationLayer(protected val seed: Long) {
    companion object {
        private const val PRIME_1 = 5938205574662135861L
        private const val PRIME_2 = 4774020943567422433L
    }

    protected var globalSeed: Long = 0
    protected var currentSeed: Long = 0

    abstract fun generate(x: Int, y: Int, width: Int, height: Int): IntArray

    fun initGlobalSeed(seed: Long) {
        globalSeed = seed
        globalSeed *= globalSeed * PRIME_1 + PRIME_2
        globalSeed += seed
        globalSeed *= globalSeed * PRIME_1 + PRIME_2
        globalSeed += seed
        globalSeed *= globalSeed * PRIME_1 + PRIME_2
        globalSeed += seed

        initParentSeed(seed)
    }

    open fun initParentSeed(seed: Long) {
    }

    protected fun initSeed(x: Int, y: Int) {
        currentSeed = globalSeed
        currentSeed *= currentSeed * PRIME_1 + PRIME_2
        currentSeed += x.toLong()
        currentSeed *= currentSeed * PRIME_1 + PRIME_2
        currentSeed += y.toLong()
        currentSeed *= currentSeed * PRIME_1 + PRIME_2
        currentSeed += x.toLong()
        currentSeed *= currentSeed * PRIME_1 + PRIME_2
        currentSeed += y.toLong()
    }

    protected inline fun seed(x: Int, y: Int, width: Int, height: Int, seedCoordinate: (localX: Int, localY: Int) -> Int): IntArray {
        val result = IntArray(width * height)
        repeat(height) { localY ->
            repeat(width) { localX ->
                initSeed(x + localX, y + localY)
                result[localX + localY * width] = seedCoordinate(localX, localY)
            }
        }
        return result
    }

    protected fun nextInt(max: Int): Int {
        var value = ((currentSeed shr 24) % max).toInt()
        if (value < 0) {
            value += max
        }

        currentSeed *= currentSeed * PRIME_1 + PRIME_2
        currentSeed += globalSeed

        return value
    }

    protected fun <T> selectRandom(vararg values: T) = values[nextInt(values.size)]
}
