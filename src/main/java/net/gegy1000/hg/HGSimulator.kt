package net.gegy1000.hg

import javafx.scene.image.WritableImage
import net.gegy1000.hg.arena.Arena
import net.gegy1000.hg.player.BasePlayerStatistics
import net.gegy1000.hg.player.Entity
import net.gegy1000.hg.player.Player
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Random
import kotlin.concurrent.fixedRateTimer

object HGSimulator {
    val LOGGER: Logger = LoggerFactory.getLogger(HGSimulator::class.java)

    val random = Random()

    val groundcoverImage = WritableImage(Arena.SIZE, Arena.SIZE)

    val entities: MutableSet<Entity> = HashSet()

    var updates: Int = 0

    fun startLoop() {
        LOGGER.info("Drawing groundcover image")

        val pixelWriter = groundcoverImage.pixelWriter
        repeat(Arena.SIZE) { localY ->
            repeat(Arena.SIZE) { localX ->
                pixelWriter.setColor(localX, localY, Arena[localX, localY].groundType.colour)
            }
        }

        LOGGER.info("Initializing update loop")

        entities.add(Player("Reddit", BasePlayerStatistics.random(Random())))

        fixedRateTimer(name = "Update Loop", daemon = false, period = Constants.TICK_RATE_MILLIS) { update() }
    }

    fun update() {
        if (updates % 10 == 0) {
            //status update
        }
        entities.forEach { it.update() }
        updates++
    }
}
