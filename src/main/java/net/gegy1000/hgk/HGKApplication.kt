package net.gegy1000.hgk

import net.gegy1000.hgk.model.ErrorModel
import net.gegy1000.hgk.model.SessionDataModel
import net.gegy1000.hgk.session.SessionManager
import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.features.Compression
import org.jetbrains.ktor.features.DefaultHeaders
import org.jetbrains.ktor.gson.GsonSupport
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.routing.Routing
import org.jetbrains.ktor.routing.get

fun Application.main() {
    install(DefaultHeaders)
    install(Compression)
    //TODO: Remove pretty printing
    install(GsonSupport) { setPrettyPrinting() }
    install(Routing) {
        get("/v1/session/{session}") {
            val sessionId = call.parameters["session"]
            val lastUpdate = call.parameters["lastUpdate"]?.toIntOrNull() ?: -1

            if (sessionId == null) {
                call.respond(ErrorModel("Missing session id"))
                return@get
            }

            val session = SessionManager[sessionId]
            if (session == null) {
                call.respond(ErrorModel("Invalid session id"))
                return@get
            }

            val snapshots = session.snapshots.filterIndexed { index, _ -> index > lastUpdate }.toTypedArray()
            val arena = if (lastUpdate == -1) {
                val tiles = session.arena.tiles.map { SessionDataModel.Tile(it.height, it.temperature, it.groundType) }
                SessionDataModel.Arena(tiles.toTypedArray())
            } else null

            call.respond(SessionDataModel(sessionId, session.updateIndex, snapshots, arena))
        }
    }
}
