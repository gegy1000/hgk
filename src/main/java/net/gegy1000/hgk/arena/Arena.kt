package net.gegy1000.hgk.arena

import net.gegy1000.hgk.arena.GroundType.CORNUCOPIA
import net.gegy1000.hgk.arena.GroundType.GROUND
import net.gegy1000.hgk.arena.GroundType.OUTSIDE
import net.gegy1000.hgk.arena.GroundType.WATER
import net.gegy1000.hgk.arena.VegetationType.FOREST
import net.gegy1000.hgk.arena.VegetationType.SHRUBLAND
import net.gegy1000.hgk.arena.generation.layer.CreatePathLayer
import net.gegy1000.hgk.arena.generation.layer.GenerateHeightsLayer
import net.gegy1000.hgk.arena.generation.layer.GenerationLayer
import net.gegy1000.hgk.arena.generation.layer.SeedBiomesLayer
import net.gegy1000.hgk.arena.generation.layer.SeedVegetationLayer
import net.gegy1000.hgk.arena.generation.layer.scale.FlatFuzzScaleLayer
import net.gegy1000.hgk.arena.generation.layer.scale.FlatScaleLayer
import net.gegy1000.hgk.session.GameSession
import java.awt.image.BufferedImage
import java.io.File
import java.util.Random
import javax.imageio.ImageIO

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
    val vegetationSeed = generationRandom.nextLong()

    val waterLevel = generationRandom.nextInt(2) + Biome.WATER.max - 1

    val tiles: Array<Tile> = generateArena()

    init {
        val heights = BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB)
        repeat(SIZE) { localY ->
            repeat(SIZE) { localX ->
                val tile = this[localX, localY]
                val height = tile.height * 2
                heights.setRGB(localX, localY, (height shl 16) or (height shl 8) or height)
            }
        }
        ImageIO.write(heights, "png", File("heights.png"))
        val ground = BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB)
        repeat(SIZE) { localY ->
            repeat(SIZE) { localX ->
                val tile = this[localX, localY]
                ground.setRGB(localX, localY, when (tile.groundType) {
                    WATER -> 0x0000FF
                    GROUND -> when (tile.vegetationType) {
                        FOREST -> 0x00AA50
                        SHRUBLAND -> 0x80AA50
                        else -> 0x00FF00
                    }
                    CORNUCOPIA -> 0xFFFF00
                    else -> 0xAABBFF
                })
            }
        }
        ImageIO.write(ground, "png", File("ground.png"))
    }

    operator fun get(x: Int, y: Int): Tile {
        if (x in COORD_RANGE && y in COORD_RANGE) {
            return tiles[x + y * SIZE]
        } else {
            return Tile(x, y, 0, GroundType.OUTSIDE, VegetationType.GRASS)
        }
    }

    fun generateArena(): Array<Tile> {
        val biomeLayer = createBiomeLayer()

        val heightLayer = createHeightLayer(biomeLayer)
        val vegetationLayer = createVegetationLayer()

        val heightmap = heightLayer.generate(0, 0, SIZE, SIZE)
        val vegetationMap = vegetationLayer.generate(0, 0, SIZE, SIZE)

        return Array(SIZE * SIZE) {
            generateTile(it % SIZE, it / SIZE, it, heightmap, vegetationMap)
        }
    }

    private fun generateTile(x: Int, y: Int, index: Int, heightmap: IntArray, vegetationMap: IntArray): Tile {
        val deltaX = x - HALF_SIZE
        val deltaY = y - HALF_SIZE
        val delta = deltaX * deltaX + deltaY * deltaY
        val height = heightmap[index]
        val vegetation = VegetationType.values()[vegetationMap[index]]
        val groundType = when {
            delta > HALF_SIZE_SQ -> OUTSIDE
            delta < CORNUCOPIA_RADIUS_SQ -> CORNUCOPIA
            height < waterLevel -> WATER
            else -> GROUND
        }
        return Tile(x, y, height, groundType, vegetation)
    }

    private fun createHeightLayer(biomeLayer: GenerationLayer): GenerationLayer {
        val heightLayer: GenerationLayer = GenerateHeightsLayer(3, 0L, biomeLayer)
        heightLayer.initGlobalSeed(generationSeed)

        return heightLayer
    }

    private fun createVegetationLayer(): GenerationLayer {
        var vegetationLayer: GenerationLayer = SeedVegetationLayer(20000000L)
        vegetationLayer = FlatScaleLayer(21000000L, vegetationLayer)
        vegetationLayer = FlatFuzzScaleLayer(22000000L, vegetationLayer)
        vegetationLayer = FlatScaleLayer(23000000L, vegetationLayer)
        vegetationLayer = FlatScaleLayer(24000000L, vegetationLayer)
        vegetationLayer = FlatFuzzScaleLayer(25000000L, vegetationLayer)
        vegetationLayer = FlatScaleLayer(26000000L, vegetationLayer)
        vegetationLayer.initGlobalSeed(vegetationSeed)

        return vegetationLayer
    }

    private fun createBiomeLayer(): GenerationLayer {
        var biomeLayer: GenerationLayer = SeedBiomesLayer(10000000L)
        biomeLayer = FlatScaleLayer(11000000L, biomeLayer)
        biomeLayer = CreatePathLayer(0L, biomeLayer)
        biomeLayer = FlatFuzzScaleLayer(12000000L, biomeLayer)
        biomeLayer = FlatScaleLayer(13000000L, biomeLayer)
        biomeLayer = FlatScaleLayer(14000000L, biomeLayer)
        biomeLayer = FlatScaleLayer(15000000L, biomeLayer)
        biomeLayer = FlatScaleLayer(16000000L, biomeLayer)
        biomeLayer.initGlobalSeed(generationSeed)

        return biomeLayer
    }
}
