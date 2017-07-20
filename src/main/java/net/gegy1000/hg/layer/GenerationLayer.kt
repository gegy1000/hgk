package net.gegy1000.hg.layer

abstract class GenerationLayer(protected val seed: Long, protected val parent: GenerationLayer? = null) {
    companion object {
        private const val PRIME_1 = 5938205574662135861L
        private const val PRIME_2 = 4774020943567422433L
    }

    protected var globalSeed: Long = 0
    protected var currentSeed: Long = 0

    abstract fun generate(x: Int, y: Int, width: Int, height: Int): FloatArray

    fun sampleParent(x: Int, y: Int, width: Int, height: Int): FloatArray {
        return this.parent?.generate(x, y, width, height) ?: FloatArray(width * height)
    }

    fun initGlobalSeed(seed: Long) {
        this.globalSeed = seed
        this.globalSeed *= this.globalSeed * PRIME_1 + PRIME_2
        this.globalSeed += this.seed
        this.globalSeed *= this.globalSeed * PRIME_1 + PRIME_2
        this.globalSeed += this.seed
        this.globalSeed *= this.globalSeed * PRIME_1 + PRIME_2
        this.globalSeed += this.seed

        this.parent?.initGlobalSeed(seed)
    }

    protected fun initSeed(x: Int, y: Int) {
        this.currentSeed = this.globalSeed
        this.currentSeed *= this.currentSeed * PRIME_1 + PRIME_2
        this.currentSeed += x.toLong()
        this.currentSeed *= this.currentSeed * PRIME_1 + PRIME_2
        this.currentSeed += y.toLong()
        this.currentSeed *= this.currentSeed * PRIME_1 + PRIME_2
        this.currentSeed += x.toLong()
        this.currentSeed *= this.currentSeed * PRIME_1 + PRIME_2
        this.currentSeed += y.toLong()
    }

    protected fun nextInt(max: Int): Int {
        var value = ((this.currentSeed shr 24) % max).toInt()
        if (value < 0) {
            value += max
        }

        this.currentSeed *= this.currentSeed * PRIME_1 + PRIME_2
        this.currentSeed += this.globalSeed

        return value
    }

    protected fun nextFloat() = nextInt(Short.MAX_VALUE.toInt()).toFloat() / Short.MAX_VALUE

    protected fun <T> selectRandom(vararg values: T) = values[nextInt(values.size)]
}
