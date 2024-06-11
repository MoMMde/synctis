package template.group.name

import com.mongodb.kotlin.client.coroutine.MongoClient

fun connectToMongoDB() = MongoClient.create(Config.MONGO_URL)
