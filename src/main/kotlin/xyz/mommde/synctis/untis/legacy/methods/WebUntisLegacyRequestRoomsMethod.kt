package xyz.mommde.synctis.untis.legacy.methods

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class WebUntisLegacyRoomsResponse(
    val id: Int,
    val name: String,
    val longName: String,
    @SerialName("foreColor")
    val foregroundColor: String? = null,
    @SerialName("backColor")
    val backgroundColor: String? = null,
)

internal object WebUntisLegacyRequestRoomsMethod : WebUntisLegacyEmptyRequestMethod<List<WebUntisLegacyRoomsResponse>>("getRooms")