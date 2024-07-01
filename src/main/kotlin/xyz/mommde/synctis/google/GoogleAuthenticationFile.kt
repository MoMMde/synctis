package xyz.mommde.synctis.google

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.auth.*
import kotlinx.serialization.encodeToString
import xyz.mommde.synctis.Config
import xyz.mommde.synctis.untis.legacy.json
import java.nio.file.Path
import kotlin.io.path.*

val logger = KotlinLogging.logger("GoogleAuthenticationFile")

fun googleAuthenticationFileOrNull(path: Path = Path(Config.Google.AUTH_FILE)): OAuthAccessTokenResponse.OAuth2? {
    logger.info { "Trying to access Google OAuth2 Credentials" }
    if (!path.exists()) {
        logger.info { "Could not find ${path.absolutePathString()}. Assuming no auth has taken place yet." }
        return null
    }

    return json.decodeFromString(path.readText())
}

fun writeOauth2Token(path: Path = Path(Config.Google.AUTH_FILE), oauthToken: OAuthAccessTokenResponse.OAuth2) {
    logger.info { "Writing Google OAuth2 Credentials" }
    path.writeText(json.encodeToString(oauthToken))
}