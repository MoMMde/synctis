package template.group.name

import com.mongodb.kotlin.client.coroutine.MongoClient
import org.koin.dsl.module

fun connectToMongoDB(url: String = Config.MONGO_URL) = MongoClient.create(url)

val repositoryModule = module {
    single<MongoClient> { connectToMongoDB() }
}