package net.gegy1000.hgk.model

import com.google.gson.annotations.SerializedName

data class SnapshotModel(
        @SerializedName("update_index") val updateIndex: Int,
        @SerializedName("status_updates") val statusUpdates: Array<String>
) {
    override fun hashCode() = updateIndex

    override fun equals(other: Any?) = other is SnapshotModel && other.updateIndex == updateIndex
}
