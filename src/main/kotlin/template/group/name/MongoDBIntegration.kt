package template.group.name

import com.mongodb.kotlin.client.coroutine.MongoClient

fun connectToMongoDB(url: String = Config.MONGO_URL) = MongoClient.create(url)
