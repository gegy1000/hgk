package net.gegy1000.hgk.session

object SessionManager {
    private val sessions = HashMap<String, GameSession>()

    operator fun get(identifier: String) = sessions[identifier]
}
