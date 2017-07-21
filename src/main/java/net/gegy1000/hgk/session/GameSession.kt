package net.gegy1000.hgk.session

import net.gegy1000.hgk.TimerConstants
import net.gegy1000.hgk.arena.Arena
import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.model.SessionDataModel
import net.gegy1000.hgk.model.SessionSetupModel
import net.gegy1000.hgk.model.SnapshotModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Random

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

    private fun update() {
        entities.forEach { it.update() }

        snapshots.add(createSnapshot())
        updateIndex++
    }

    private fun createSnapshot(): SnapshotModel {
        val entities = entities.map { SnapshotModel.Entity(it.type, it.tileX, it.tileY, it.model) }.toTypedArray()
        return SnapshotModel(updateIndex, entities)
    }

    @Synchronized
    fun catchUpUpdates() {
        while (updateIndex < expectedUpdate) {
            update()
        }
    }

    fun sampleSnapshots(lastUpdate: Int): List<SnapshotModel> {
        catchUpUpdates()
        return snapshots.filterIndexed { i, _ -> i > lastUpdate }
    }

    fun createSetupInfo(): SessionSetupModel {
        val tiles = LongArray(arena.tiles.size) { arena.tiles[it].toLong() }
        return SessionSetupModel(identifier, SessionDataModel.Arena(tiles), startTime, TimerConstants.TICK_RATE_MILLIS, TimerConstants.TICKS_PER_DAY)
    }
}
