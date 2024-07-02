package xyz.mommde.synctis.google.oauth2

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.auth.*

suspend fun isTokenValid(httpClient: HttpClient, oAuth2: OAuthAccessTokenResponse.OAuth2): Boolean {
    val tokenCheckup = httpClient.get("https://oauth2.googleapis.com/tokeninfo") {
        parameter("access_token", oAuth2.accessToken)
    }
    println(tokenCheckup.bodyAsText())
    return true
}