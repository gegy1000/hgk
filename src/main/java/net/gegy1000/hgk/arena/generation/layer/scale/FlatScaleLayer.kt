package net.gegy1000.hgk.arena.generation.layer.scale

import net.gegy1000.hgk.arena.generation.layer.GenerationLayer

class FlatScaleLayer(seed: Long, parent: GenerationLayer) : ScaleLayer(seed, parent) {
    override fun select(values: IntArray): Int {
        return when (values.size) {
            4 -> values.sortedByDescending { value -> values.count { it == value } }.first()
            else -> values[nextInt(values.size)]
        }
    }
}
