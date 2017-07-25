package net.gegy1000.hgk.session

import net.gegy1000.hgk.TimerConstants
import net.gegy1000.hgk.arena.Arena
import net.gegy1000.hgk.entity.Pronoun
import net.gegy1000.hgk.entity.component.AIComponent
import net.gegy1000.hgk.entity.component.InfluenceComponent
import net.gegy1000.hgk.entity.component.InfluenceMapComponent
import net.gegy1000.hgk.entity.component.LivingComponent
import net.gegy1000.hgk.entity.component.MetabolismComponent
import net.gegy1000.hgk.entity.component.MovementComponent
import net.gegy1000.hgk.entity.component.NavigationComponent
import net.gegy1000.hgk.entity.component.PlayerComponent
import net.gegy1000.hgk.entity.component.PositionComponent
import net.gegy1000.hgk.entity.component.ReferralComponent
import net.gegy1000.hgk.entity.component.SleepComponent
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
        session.entityEngine.entities += creationData.players.map {
            session.entityEngine.builder.apply {
                components += LivingComponent()
                components += PositionComponent(Arena.SIZE / 2.0, Arena.SIZE / 2.0)
                components += PlayerComponent.random((session.random))
                components += ReferralComponent(it.name ?: "unnamed player", it.pronoun ?: Pronoun.NEUTRAL)
                components += InfluenceComponent(70, 30)
                components += InfluenceMapComponent()
                components += SleepComponent()
                components += MetabolismComponent()
                components += NavigationComponent(14, 10, 70)
                components += MovementComponent()
                components += AIComponent()
            }.build()
        }
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
        sessions.filter { time - it.value.lastCall > TimerConstants.SESSION_TIMEOUT_MILLIS }
                .forEach { sessions.remove(it.key) }
    }
}
