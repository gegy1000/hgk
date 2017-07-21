package net.gegy1000.hgk.model

import com.google.gson.annotations.SerializedName

data class SessionSetupModel(
        @SerializedName("identifier") val identifier: String,
        @SerializedName("arena") val arena: SessionDataModel.Arena,
        @SerializedName("start_time") val startTime: Long,
        @SerializedName("tick_rate_millis") val tickRateMillis: Long,
        @SerializedName("ticks_per_day") val ticksPerDay: Int
)
