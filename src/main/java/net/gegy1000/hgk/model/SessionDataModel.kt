package net.gegy1000.hgk.model

import com.google.gson.annotations.SerializedName
import net.gegy1000.hgk.arena.GroundType
import java.util.Arrays

data class SessionDataModel(
        @SerializedName("identifier") val identifier: String,
        @SerializedName("current_update") val currentUpdate: Int,
        @SerializedName("snapshots") val snapshots: Array<SnapshotModel>,
        @SerializedName("arena") val arena: Arena? = null
) {
    override fun hashCode() = identifier.hashCode() shl 16 or Arrays.hashCode(snapshots)

    override fun equals(other: Any?) = other is SessionDataModel && other.identifier == identifier && Arrays.equals(other.snapshots, snapshots)

    class Arena(
            @SerializedName("tiles") val tiles: Array<Tile>
    )

    data class Tile(
            @SerializedName("height") val height: Int,
            @SerializedName("temperature") val temperature: Float,
            @SerializedName("ground_type") val groundType: GroundType
    )
}
