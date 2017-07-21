package net.gegy1000.hgk.model

import net.gegy1000.hgk.entity.BasePlayerStatistics

class SessionCreationModel(val players: Array<Player>, val seed: Long) {
    data class Player(val name: String, val statistics: BasePlayerStatistics?)
}
