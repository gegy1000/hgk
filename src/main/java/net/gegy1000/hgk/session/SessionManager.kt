package net.gegy1000.hgk.session

import net.gegy1000.hgk.TimerConstants
import net.gegy1000.hgk.entity.BasePlayerStatistics
import net.gegy1000.hgk.entity.Player
import net.gegy1000.hgk.model.SessionCreationModel
import java.util.Random
import kotlin.concurrent.fixedRateTimer

object SessionManager {
    private const val IDENTIFIER_CHARS = "abcdefghijklmnopqrstuvwxy0123456789"
    private const val IDENTIFIER_LENGTH = 16

    private val sessions = HashMap<String, GameSession>()

    private val random = Random()

    val activeSessionCount: Int
        get() = sessions.size

    init {
        fixedRateTimer("Idle Update", daemon = true, period = 5000) {
            val time = System.currentTimeMillis()
            sessions.filter { time - it.value.lastCall > 5000 }.values.forEach { it.catchUpUpdates() }
            SessionManager.cleanup()
        }
    }

    fun create(creationData: SessionCreationModel): GameSession {
        val identifier = generateIdentifier()
        val session = GameSession(identifier, creationData.seed)
        session.entities.addAll(creationData.players.map { Player(session.arena, it.name, it.statistics ?: BasePlayerStatistics.random(session.random)) })
        sessions.put(identifier, session)
        return session
    }

    private fun generateIdentifier(): String {
        val builder = StringBuilder()
        repeat(IDENTIFIER_LENGTH) {
            builder.append(IDENTIFIER_CHARS[random.nextInt(IDENTIFIER_CHARS.length)])
        }
        return builder.toString()
    }

    operator fun get(identifier: String) = sessions[identifier]

    fun cleanup() {
        val time = System.currentTimeMillis()
        sessions.asSequence().filter { time - it.value.lastCall > TimerConstants.SESSION_TIMEOUT_MILLIS }
                .forEach { sessions.remove(it.key) }
    }
}
