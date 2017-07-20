package net.gegy1000.hgk.arena

import net.gegy1000.hgk.arena.GroundType.BUSHES
import net.gegy1000.hgk.arena.GroundType.CORNUCOPIA
import net.gegy1000.hgk.arena.GroundType.FOREST
import net.gegy1000.hgk.arena.GroundType.OPEN
import net.gegy1000.hgk.arena.GroundType.OUTSIDE
import net.gegy1000.hgk.arena.GroundType.WATER
import net.gegy1000.hgk.arena.generation.layer.CreatePathLayer
import net.gegy1000.hgk.arena.generation.layer.GenerationLayer
import net.gegy1000.hgk.arena.generation.layer.ScaleLayer
import net.gegy1000.hgk.arena.generation.layer.SeedNoiseLayer
import net.gegy1000.hgk.session.GameSession
import java.util.Random

class Arena(val session: GameSession, seed: Long) {
    companion object {
        const val SIZE = 512

        const val HALF_SIZE = SIZE / 2

        const val HALF_SIZE_SQ = HALF_SIZE * HALF_SIZE
        const val CORNUCOPIA_RADIUS_SQ = 5 * 5

        val COORD_RANGE = 0..SIZE - 1
    }

    val generationRandom = Random(seed)
    val generationSeed = generationRandom.nextLong()

    val waterLevel = generationRandom.nextInt(5) + 7

    val tiles: Array<Tile> = generateArena()

    operator fun get(x: Int, y: Int): Tile {
        if (x in COORD_RANGE && y in COORD_RANGE) {
            return tiles[x + y * SIZE]
        } else {
            return Tile(x, y, 0, 0.0F, GroundType.OUTSIDE)
        }
    }

    fun generateArena(): Array<Tile> {
        val heightLayer = createHeightLayer()
        val temperatureLayer = createTemperatureLayer()
        val vegetationLayer = createVegetationLayer()

        val heightmap = heightLayer.generate(0, 0, SIZE, SIZE)
        val temperatureMap = temperatureLayer.generate(0, 0, SIZE, SIZE)
        val vegetationMap = vegetationLayer.generate(0, 0, SIZE, SIZE)

        return Array(SIZE * SIZE) {
            generateTile(it % SIZE, it / SIZE, it, heightmap, temperatureMap, vegetationMap)
        }
    }

    private fun generateTile(x: Int, y: Int, index: Int, heightmap: Array<Float>, temperatureMap: Array<Float>, vegetationMap: Array<Float>): Tile {
        val deltaX = x - HALF_SIZE
        val deltaY = y - HALF_SIZE
        val delta = deltaX * deltaX + deltaY * deltaY
        val tileTemperature = temperatureMap[index]
        val height = heightmap[index].toInt()
        var tileGround = when {
            height < waterLevel -> WATER
            tileTemperature < -0.2F -> if (vegetationMap[index] < 0.1) BUSHES else OPEN
            tileTemperature < 0.8F -> if (vegetationMap[index] < 0.3) OPEN else if (vegetationMap[index] < 0.6) BUSHES else FOREST
            else -> OPEN
        }
        if (delta > HALF_SIZE_SQ) {
            tileGround = OUTSIDE
        } else if (delta < CORNUCOPIA_RADIUS_SQ) {
            tileGround = CORNUCOPIA
        }
        return Tile(x, y, height, tileTemperature, tileGround)
    }

    private fun createHeightLayer(): GenerationLayer<Float> {
        var heightLayer: GenerationLayer<Float> = SeedNoiseLayer(0.0F, 50.0F, 10000L)
        heightLayer = ScaleLayer(1000L, heightLayer)
        heightLayer = ScaleLayer(2000L, heightLayer)
        heightLayer = CreatePathLayer(waterLevel + 1.0F, 0, heightLayer)
        heightLayer = ScaleLayer(3000L, heightLayer)
        heightLayer = ScaleLayer(4000L, heightLayer)
        heightLayer = ScaleLayer(5000L, heightLayer)
        heightLayer = ScaleLayer(6000L, heightLayer)
        heightLayer.initGlobalSeed(generationSeed)
        return heightLayer
    }

    private fun createTemperatureLayer(): GenerationLayer<Float> {
        var temperatureLayer: GenerationLayer<Float> = SeedNoiseLayer(-1.0F, 1.0F, 20000L)
        temperatureLayer = ScaleLayer(1000L, temperatureLayer)
        temperatureLayer = ScaleLayer(2000L, temperatureLayer)
        temperatureLayer = ScaleLayer(3000L, temperatureLayer)
        temperatureLayer = ScaleLayer(4000L, temperatureLayer)
        temperatureLayer = ScaleLayer(5000L, temperatureLayer)
        temperatureLayer = ScaleLayer(6000L, temperatureLayer)
        temperatureLayer.initGlobalSeed(generationSeed)
        return temperatureLayer
    }

    private fun createVegetationLayer(): GenerationLayer<Float> {
        var vegetationLayer: GenerationLayer<Float> = SeedNoiseLayer(0.0F, 1.0F, 30000L)
        vegetationLayer = ScaleLayer(1000L, vegetationLayer)
        vegetationLayer = ScaleLayer(2000L, vegetationLayer)
        vegetationLayer = ScaleLayer(3000L, vegetationLayer)
        vegetationLayer = ScaleLayer(4000L, vegetationLayer)
        vegetationLayer = ScaleLayer(5000L, vegetationLayer)
        vegetationLayer.initGlobalSeed(generationSeed)
        return vegetationLayer
    }
}
