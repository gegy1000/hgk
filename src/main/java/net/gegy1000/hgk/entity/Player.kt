package net.gegy1000.hgk.entity

import com.google.gson.annotations.SerializedName
import net.gegy1000.hgk.MetabolismConstants
import net.gegy1000.hgk.arena.Arena
import net.gegy1000.hgk.entity.ai.AISystem
import net.gegy1000.hgk.model.PlayerInfoModel
import net.gegy1000.hgk.session.StatusMessage
import net.gegy1000.hgk.session.StatusMessage.Property
import java.util.EnumMap
import java.util.Random

class Player(arena: Arena, val statistics: PlayerStatistics, val info: PlayerInfoModel) : Entity(arena, Arena.SIZE / 2, Arena.SIZE / 2) {
    override val type = "player"

    override val model: Any?
        get() = Player.Model(info.name, metabolism.energy, MetabolismConstants.MAX_FOOD, metabolism.hydration, MetabolismConstants.MAX_WATER, dead)

    val metabolism = Metabolism(this)
    val ai = AISystem(this)

    var sleepTime = 0

    var dead = false

    val tilesPerTick: Float
        get() = statistics.speed * (1.0F - (bodyPartDamage[BodyPart.LEGS] ?: 0.0F)) * ((metabolism.stamina / MetabolismConstants.MAX_STAMINA.toFloat()) * 0.5F + 0.5F)

    val random: Random
        get() = arena.session.random

    private val bodyPartDamage: MutableMap<BodyPart, Float> = EnumMap(BodyPart::class.java)

    override fun update() {
        if (!dead) {
            ai.update()
            metabolism.update(ai.moved)

            if (sleepTime > 0) {
                sleepTime--
                if (sleepTime == 0) {
                    post("event.awake")
                }
            }

            if (sleepTime <= 0) {
                super.update()
            }
        }
    }

    fun post(key: String) = post(arrayOf(key))

    fun post(keys: Array<String>, properties: Array<Property> = emptyArray()) {
        val message = StatusMessage(keys, properties + StatusMessage.getDefaultProperties(info)) { it[random.nextInt(it.size)] }
        arena.session.post(message)
    }

    enum class BodyPart {
        HEAD,
        CHEST,
        ARMS,
        LEGS
    }

    data class Model(
            @SerializedName("name") val name: String,
            @SerializedName("energy") val energy: Int,
            @SerializedName("max_energy") val maxEnergy: Int,
            @SerializedName("hydration") val hydration: Int,
            @SerializedName("max_hydration") val maxHydration: Int,
            @SerializedName("dead") val dead: Boolean
    )
}
