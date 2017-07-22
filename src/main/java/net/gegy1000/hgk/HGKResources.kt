package net.gegy1000.hgk

import com.google.gson.Gson
import net.gegy1000.hgk.model.StatusMessagesModel
import java.io.InputStreamReader

object HGKResources {
    private val gson = Gson()

    private val statusMappings = HashMap<String, Array<String>>()

    init {
        InputStreamReader(HGKResources::class.java.getResourceAsStream("/status_messages.json")).use {
            gson.fromJson(it, StatusMessagesModel::class.java).messages.forEach {
                statusMappings.put(it.name, it.options)
            }
        }
    }

    fun getStatus(key: String) = statusMappings[key] ?: emptyArray()
}
