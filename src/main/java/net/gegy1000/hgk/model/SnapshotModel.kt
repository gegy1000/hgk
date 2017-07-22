package net.gegy1000.hgk.model

import com.google.gson.annotations.SerializedName

data class SnapshotModel(
        @SerializedName("update_index") val updateIndex: Int,
        @SerializedName("entities") val entities: Array<SnapshotModel.Entity>,
        @SerializedName("status_updates") val statusUpdates: Array<String>
) {
    override fun hashCode() = updateIndex

    override fun equals(other: Any?) = other is SnapshotModel && other.updateIndex == updateIndex

    data class Entity(
            @SerializedName("type") val type: String,
            @SerializedName("x") val x: Int,
            @SerializedName("y") val y: Int,
            @SerializedName("data") val data: Any?
    )
}
