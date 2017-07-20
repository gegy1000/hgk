package net.gegy1000.hgk.session

import net.gegy1000.hgk.arena.Arena
import net.gegy1000.hgk.entity.Entity
import net.gegy1000.hgk.model.SnapshotModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Random

data class GameSession(val identifier: String, val seed: Long) {
    val logger: Logger = LoggerFactory.getLogger("session-$identifier")

    val arena = Arena(this, seed)
    val random = Random(seed)

    val entities = ArrayList<Entity>()

    var updateIndex = 0

    val snapshots = ArrayList<SnapshotModel>()

    fun update() {
        snapshots.add(createSnapshot())
        updateIndex++
    }

    private fun createSnapshot(): SnapshotModel {
        val entities = entities.map { SnapshotModel.Entity(it.type, it.tileX, it.tileY, it.model) }.toTypedArray()
        return SnapshotModel(updateIndex, entities)
    }
}
