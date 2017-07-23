package net.gegy1000.hgk.session

import net.gegy1000.hgk.TimerConstants
import net.gegy1000.hgk.arena.Arena
import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.model.SessionSetupModel
import net.gegy1000.hgk.model.SnapshotModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Random
import kotlin.system.measureTimeMillis

data class GameSession(val identifier: String, val seed: Long) {
    val logger: Logger = LoggerFactory.getLogger("session-$identifier")

    val arena = Arena(this, seed)
    val random = Random(seed)

    val startTime = System.currentTimeMillis()
    val entities = ArrayList<Entity>()

    var lastCall: Long = startTime

    val expectedUpdate: Int
        get() = ((System.currentTimeMillis() - startTime) / TimerConstants.TICK_RATE_MILLIS).toInt()

    var updateIndex = 0

    val snapshots = ArrayList<SnapshotModel>()

    val statusUpdates = ArrayList<String>()

    fun post(message: StatusMessage) {
        val status = message.toString()
        logger.info(status)
        statusUpdates.add(status)
    }

    private fun update() {
        statusUpdates.clear()

        entities.forEach {
            it.update()
            if (updateIndex % TimerConstants.PATH_RECALCULATE_TICKS == 0) {
                it.influenceMap.update()
            }
        }

        snapshots.add(createSnapshot())
        updateIndex++
    }

    private fun skipTicks(count: Int) {
        statusUpdates.clear()
        repeat(count) {
            snapshots.add(createSnapshot())
            updateIndex++
        }
    }

    private fun createSnapshot(): SnapshotModel {
        val entities = entities.map { SnapshotModel.Entity(it.type, it.tileX, it.tileY, it.model) }.toTypedArray()
        return SnapshotModel(updateIndex, entities, statusUpdates.toTypedArray())
    }

    @Synchronized
    fun catchUpUpdates() {
        while (updateIndex < expectedUpdate + 5) {
            val time = measureTimeMillis { update() }
            if (time > TimerConstants.TICK_RATE_MILLIS) {
                val skip = time / TimerConstants.TICK_RATE_MILLIS
                logger.info("Update took $time millis, skipping $skip ticks")
                skipTicks(skip.toInt())
            }
        }
    }

    fun sampleSnapshots(lastUpdate: Int): List<SnapshotModel> {
        catchUpUpdates()
        return snapshots.filterIndexed { i, _ -> i > lastUpdate }
    }

    fun createSetupInfo(): SessionSetupModel {
        val tiles = IntArray(arena.tiles.size) { arena.tiles[it].toInt() }
        return SessionSetupModel(identifier, SessionSetupModel.Arena(tiles), startTime, TimerConstants.TICK_RATE_MILLIS, TimerConstants.TICKS_PER_DAY)
    }
}
