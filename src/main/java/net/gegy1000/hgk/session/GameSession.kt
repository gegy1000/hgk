package net.gegy1000.hgk.session

import net.gegy1000.hgk.TimerConstants
import net.gegy1000.hgk.arena.Arena
import net.gegy1000.hgk.entity.EntityEngine
import net.gegy1000.hgk.model.SessionSetupModel
import net.gegy1000.hgk.model.SnapshotModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Random
import kotlin.system.measureTimeMillis

data class GameSession(val identifier: String, val seed: Long) {
    val logger: Logger = LoggerFactory.getLogger("session-$identifier")
    val arena = Arena(this, seed)

    val entityEngine = EntityEngine(arena)

    val random = Random(seed)
    val startTime = System.currentTimeMillis()

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

        entityEngine.update()

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
        return SnapshotModel(updateIndex, statusUpdates.toTypedArray())
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
        val tiles = LongArray(arena.tiles.size) { arena.tiles[it].toLong() }
        return SessionSetupModel(identifier, SessionSetupModel.Arena(Arena.SIZE, tiles), startTime, TimerConstants.TICK_RATE_MILLIS, TimerConstants.TICKS_PER_DAY)
    }
}
