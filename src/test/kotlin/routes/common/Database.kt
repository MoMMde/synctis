package routes.common

import template.group.name.connectToMongoDB
import kotlin.test.Test
import kotlin.test.assertTrue


class Database {
    private val databaseUrl = "mongodb://root:super-secure-password@localhost:27017/?authSource=admin"
    @Test
    fun canConnectToDatabase() {
        connectToMongoDB(databaseUrl)
        assertTrue(true, "Connection did not throw an exception")
    }
}