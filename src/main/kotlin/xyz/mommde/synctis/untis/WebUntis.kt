package xyz.mommde.synctis.untis

import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import xyz.mommde.synctis.Config
import xyz.mommde.synctis.calendar.SynctisCalendarEvent

// https://untis-sr.ch/wp-content/uploads/2019/11/2018-09-20-WebUntis_JSON_RPC_API.pdf
internal interface WebUntisApi : KoinComponent {
    // Logs in and store the session token in the implementation instance.
    // Returns true if successfull, otherwise false
    suspend fun login(
        username: String = Config.WebUntis.USERNAME,
        password: String = Config.WebUntis.PASSWORD
    ): Boolean
    // Terminates current session
    suspend fun logout()
    // Returns the Calendar for the week this day is in
    suspend fun getCalendarForWeek(week: LocalDate): List<SynctisCalendarEvent>
}