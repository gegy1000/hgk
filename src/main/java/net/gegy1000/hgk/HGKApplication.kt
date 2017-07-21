package net.gegy1000.hgk

import net.gegy1000.hgk.model.ErrorModel
import net.gegy1000.hgk.model.SessionCreationModel
import net.gegy1000.hgk.model.SessionDataModel
import net.gegy1000.hgk.session.SessionManager
import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.features.Compression
import org.jetbrains.ktor.features.DefaultHeaders
import org.jetbrains.ktor.gson.GsonSupport
import org.jetbrains.ktor.request.receive
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.routing.Routing
import org.jetbrains.ktor.routing.get
import org.jetbrains.ktor.routing.post

fun Application.main() {
    install(DefaultHeaders)
    install(Compression)
    install(GsonSupport)
    install(Routing) {
        get("/v1/snapshot/{session}/{lastUpdate}") {
            val sessionId = call.parameters["session"]
            val lastUpdate = call.parameters["lastUpdate"]?.toIntOrNull()
            val maxSnapshots = call.parameters["maxSnapshots"]?.toIntOrNull() ?: 5

            val session = SessionManager[sessionId ?: ""]
            if (session == null) {
                call.respond(ErrorModel("Invalid session id"))
                return@get
            }

            if (lastUpdate == null) {
                call.respond(ErrorModel("Missing lastUpdate parameter"))
                return@get
            }

            val snapshots = session.sampleSnapshots(lastUpdate)

            session.lastCall = System.currentTimeMillis()

            call.respond(SessionDataModel(session.identifier, session.updateIndex, snapshots.filterIndexed { i, _ -> i > +snapshots.size - maxSnapshots }.toTypedArray()))
        }
        get("/v1/session/{session}") {
            val sessionId = call.parameters["session"]

            val session = SessionManager[sessionId ?: ""]
            if (session == null) {
                call.respond(ErrorModel("Invalid session id"))
                return@get
            }

            call.respond(session.createSetupInfo())
        }
        post("/v1/create") {
            val creationData = call.receive<SessionCreationModel>()
            if (creationData.players.isEmpty()) {
                call.respond(ErrorModel("Can't start session with empty player list"))
                return@post
            }
            SessionManager.cleanup()
            if (SessionManager.activeSessionCount < 10) {
                val session = SessionManager.create(creationData)
                call.respond(SessionDataModel(session.identifier, -1, emptyArray()))
            } else {
                call.respond(ErrorModel("Could not create session, maximum running limit reached"))
            }
        }
    }
}
