package net.gegy1000.hgk.arena.generation.layer.scale

import net.gegy1000.hgk.arena.generation.layer.GenerationLayer

class AverageScaleLayer(seed: Long, parent: GenerationLayer) : ScaleLayer(seed, parent) {
    override fun select(values: IntArray): Int {
        return values.average().toInt()
    }
}
