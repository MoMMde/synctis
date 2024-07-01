package xyz.mommde.synctis.google.oauth2

import dev.schlaubi.stdx.coroutines.suspendLazy
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.auth.*
import xyz.mommde.synctis.Config

suspend fun AuthenticationConfig.googleOAuthHandler(httpClient: HttpClient) {
    val discoveryDocument = httpClient
        .get("https://accounts.google.com/.well-known/openid-configuration")
        .also {
            if (!it.status.isSuccess())
                error(".well-known/openid-configuration failed to obtain Google Oauth2 flow endpoints")
        }
        .body<DiscoveryDocument>()

    oauth("google-oauth") {
        urlProvider = { "http://${Config.HOST}:${Config.PORT}/callback" }
        providerLookup = {
            OAuthServerSettings.OAuth2ServerSettings(
                name = "google",
                authorizeUrl = discoveryDocument.authorizationEndpoint,
                accessTokenUrl = discoveryDocument.tokenEndpoint,
                requestMethod = HttpMethod.Post,
                clientId = Config.Google.CLIENT_ID,
                clientSecret = Config.Google.CLIENT_SECRET,
                defaultScopes = googleOauthScopes,
                extraAuthParameters = listOf("access_type" to "offline"),
            )
        }
        client = httpClient
    }
}