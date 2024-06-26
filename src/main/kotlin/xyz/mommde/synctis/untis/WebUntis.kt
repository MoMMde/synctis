package xyz.mommde.synctis.untis

import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.extension.KoinExtension

// https://untis-sr.ch/wp-content/uploads/2019/11/2018-09-20-WebUntis_JSON_RPC_API.pdf
internal interface WebUntisApi : KoinComponent {
    // Logs in and store the session token in the implementation instance.
    // Returns true if successfull, otherwise false
    suspend fun login(): Boolean
    suspend fun logout()
}