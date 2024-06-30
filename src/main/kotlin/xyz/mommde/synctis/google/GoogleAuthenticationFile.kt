package xyz.mommde.synctis.google

import kotlinx.serialization.Serializable
import xyz.mommde.synctis.Config
import xyz.mommde.synctis.untis.legacy.json
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readText

@Serializable
data class GoogleAuthenticationData(
    val accessToken: String,
    val scope: String,
    val tokenType: String,
    val expiresIn: Int,
    val refreshToken: String
)

fun googleAuthenticationFileOrNull(path: Path = Path(Config.Google.AUTH_FILE)): GoogleAuthenticationData? {
    if (!path.exists())
        return null

    return json.decodeFromString(path.readText())
}