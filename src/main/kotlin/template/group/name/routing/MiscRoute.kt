package template.group.name.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import template.group.name.Config
import xyz.mommde.synctis.generated.BuildConfig

@Serializable
internal data class MiscResponse(
    val version: String,
    @SerialName("git_sha")
    val gitSha: String?,
    @SerialName("git_branch")
    val gitBranch: String?,
    @SerialName("dev_mode")
    val devMode: Boolean
)

fun Route.MiscRouting() {
    get("/misc") {
        call.respond(MiscResponse(
            BuildConfig.VERSION,
            BuildConfig.GIT_SHA,
            BuildConfig.GIT_BRANCH,
            Config.DEBUG
        ))
    }
}