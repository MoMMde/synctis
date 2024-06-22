package template.group.name.repository

import org.bson.types.ObjectId

interface CollectableRepository<T> {
    suspend fun save(entity: T)
    suspend fun remove(entity: T)
    suspend fun findAll(): List<T>
    suspend fun findById(id: ObjectId): T?
}