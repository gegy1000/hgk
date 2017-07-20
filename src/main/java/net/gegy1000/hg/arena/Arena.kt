package net.gegy1000.hg.arena

import net.gegy1000.hg.layer.CreatePathLayer
import net.gegy1000.hg.layer.GenerationLayer
import net.gegy1000.hg.layer.ScaleLayer
import net.gegy1000.hg.layer.SeedNoiseLayer
import java.util.Random

object Arena {
    const val SIZE = 512

    val random = Random()
    val seed = random.nextLong()

    val tiles: Array<Tile> = generateArena()

    operator fun get(x: Int, y: Int): Tile {
        if (x in 0..SIZE - 1 && y in 0..SIZE - 1) {
            return tiles[x + y * SIZE]
        } else {
            return Tile(x, y, 0, 0.0F, GroundType.OUTSIDE)
        }
    }

    fun generateArena(): Array<Tile> {
        val waterLevel = random.nextInt(5) + 7

        var heightLayer: GenerationLayer = SeedNoiseLayer(0.0F, 50.0F, 10000L)
        heightLayer = ScaleLayer(1000L, heightLayer)
        heightLayer = ScaleLayer(2000L, heightLayer)
        heightLayer = CreatePathLayer(waterLevel + 1.0F, 0, heightLayer)
        heightLayer = ScaleLayer(3000L, heightLayer)
        heightLayer = ScaleLayer(4000L, heightLayer)
        heightLayer = ScaleLayer(5000L, heightLayer)
        heightLayer = ScaleLayer(6000L, heightLayer)
        heightLayer.initGlobalSeed(seed)

        var temperatureLayer: GenerationLayer = SeedNoiseLayer(-1.0F, 1.0F, 20000L)
        temperatureLayer = ScaleLayer(1000L, temperatureLayer)
        temperatureLayer = ScaleLayer(2000L, temperatureLayer)
        temperatureLayer = ScaleLayer(3000L, temperatureLayer)
        temperatureLayer = ScaleLayer(4000L, temperatureLayer)
        temperatureLayer = ScaleLayer(5000L, temperatureLayer)
        temperatureLayer = ScaleLayer(6000L, temperatureLayer)
        temperatureLayer.initGlobalSeed(seed)

        var vegetationLayer: GenerationLayer = SeedNoiseLayer(0.0F, 1.0F, 30000L)
        vegetationLayer = ScaleLayer(1000L, vegetationLayer)
        vegetationLayer = ScaleLayer(2000L, vegetationLayer)
        vegetationLayer = ScaleLayer(3000L, vegetationLayer)
        vegetationLayer = ScaleLayer(4000L, vegetationLayer)
        vegetationLayer = ScaleLayer(5000L, vegetationLayer)
        vegetationLayer.initGlobalSeed(seed)

        val heights = heightLayer.generate(0, 0, SIZE, SIZE)
        val temperature = temperatureLayer.generate(0, 0, SIZE, SIZE)
        val vegetation = vegetationLayer.generate(0, 0, SIZE, SIZE)

        val halfSize = Arena.SIZE / 2
        val halfSizeSqr = halfSize * halfSize

        val cornucopiaRadiusSqr = 25

        return Array(SIZE * SIZE) {
            val x = it % SIZE
            val y = it / SIZE
            val deltaX = x - halfSize
            val deltaY = y - halfSize
            val delta = deltaX * deltaX + deltaY * deltaY
            val tileTemperature = temperature[it]
            val height = heights[it].toInt()
            var tileGround = when {
                height < waterLevel -> GroundType.WATER
                tileTemperature < -0.2F -> if (vegetation[it] < 0.1) GroundType.BUSHES else GroundType.OPEN
                tileTemperature < 0.8F -> if (vegetation[it] < 0.3) GroundType.OPEN else if (vegetation[it] < 0.6) GroundType.BUSHES else GroundType.FOREST
                else -> GroundType.OPEN
            }
            if (delta > halfSizeSqr) {
                tileGround = GroundType.OUTSIDE
            } else if (delta < cornucopiaRadiusSqr) {
                tileGround = GroundType.CORNUCOPIA
            }
            Tile(x, y, height, tileTemperature, tileGround)
        }
    }
}
