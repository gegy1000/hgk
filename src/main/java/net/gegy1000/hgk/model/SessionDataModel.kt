package net.gegy1000.hgk.model

import com.google.gson.annotations.SerializedName
import java.util.Arrays

data class SessionDataModel(
        @SerializedName("identifier") val identifier: String,
        @SerializedName("current_update") val currentUpdate: Int,
        @SerializedName("snapshots") val snapshots: Array<SnapshotModel>
) {
    override fun hashCode() = identifier.hashCode() shl 16 or Arrays.hashCode(snapshots)

    override fun equals(other: Any?) = other is SessionDataModel && other.identifier == identifier && Arrays.equals(other.snapshots, snapshots)

    class Arena(
            @SerializedName("tiles") val tiles: LongArray
    )
}
