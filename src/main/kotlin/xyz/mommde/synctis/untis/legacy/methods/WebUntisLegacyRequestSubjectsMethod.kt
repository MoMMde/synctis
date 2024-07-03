package xyz.mommde.synctis.untis.legacy.methods

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class WebUntisLegacySubjectResponse(
    val id: Int,
    val name: String,
    val longName: String,
    val alternateName: String,
    val active: Boolean = true,
    @SerialName("foreColor")
    val foregroundColor: String? = null,
    @SerialName("backColor")
    val backgroundColor: String? = null,
)

internal object WebUntisLegacyRequestSubjectsMethod : WebUntisLegacyEmptyRequestMethod<List<WebUntisLegacySubjectResponse>>("getSubjects")