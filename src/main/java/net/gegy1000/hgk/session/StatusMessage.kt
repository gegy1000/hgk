package net.gegy1000.hgk.session

import net.gegy1000.hgk.HGKResources
import net.gegy1000.hgk.model.PlayerInfoModel

class StatusMessage(val keys: Array<String>, val properties: Array<Property>, val selector: (Array<String>) -> String) {
    companion object {
        fun getDefaultProperties(playerInfo: PlayerInfoModel): Array<Property> {
            return arrayOf(Property("name", playerInfo.name)) + playerInfo.pronoun.properties
        }
    }

    override fun toString(): String {
        val joined = keys.map { key ->
            val options = HGKResources.getStatus(key)
            if (options.isEmpty()) "" else selector(options).formatProperties(*properties)
        }.filterNot { it.isNullOrBlank() }.joinToString(" ")
        if (joined.isEmpty()) {
            return keys.joinToString(" ")
        }
        return "${joined[0].toUpperCase()}${joined.substring(1)}."
    }

    private fun String.formatProperties(vararg properties: Property): String {
        var result = this
        properties.forEach { result = result.replace("{${it.key}}", it.value) }
        return result
    }

    class Property(val key: String, val value: String)
}
