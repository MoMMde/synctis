package xyz.mommde.synctis.google

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.auth.*
import kotlinx.serialization.encodeToString
import xyz.mommde.synctis.Config
import xyz.mommde.synctis.google.oauth2.OAuth2DataHolder
import xyz.mommde.synctis.google.oauth2.fromOAuth2DataHolder
import xyz.mommde.synctis.google.oauth2.toOAuth2DataHolder
import xyz.mommde.synctis.json
import java.nio.file.Path
import kotlin.io.path.*

private val logger = KotlinLogging.logger("GoogleAuthenticationFile")

fun googleAuthenticationFileOrNull(path: Path = Path(Config.Google.AUTH_FILE)): OAuthAccessTokenResponse.OAuth2? {
    logger.debug { "Trying to access Google OAuth2 Credentials" }
    if (!path.exists()) {
        logger.info { "Could not find ${path.absolutePathString()}. Assuming no auth has taken place yet." }
        return null
    }
    logger.debug { "Found Google auth file. Loading ${path.absolutePathString()}" }
    return json.decodeFromString<OAuth2DataHolder>(path.readText()).fromOAuth2DataHolder()
}

fun writeOauth2Token(path: Path = Path(Config.Google.AUTH_FILE), oauthToken: OAuthAccessTokenResponse.OAuth2) {
    logger.info { "Writing Google OAuth2 Credentials" }
    path.writeText(json.encodeToString(oauthToken.toOAuth2DataHolder()))
}