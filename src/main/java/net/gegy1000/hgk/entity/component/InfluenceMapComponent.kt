package net.gegy1000.hgk.entity.component

import net.gegy1000.hgk.arena.Arena

class InfluenceMapComponent : EntityComponent {
    private val nodes = ShortArray(Arena.SIZE * Arena.SIZE)

    operator fun set(x: Int, y: Int, value: Short) {
        nodes[x + y * Arena.SIZE] = value
    }

    operator fun get(x: Int, y: Int) = nodes[x + y * Arena.SIZE].toInt()
}
