package com.eweise.domain.repository

import java.time.OffsetDateTime.now

import com.eweise.domain.model.{Entity, ID}

trait CrudRepository[A <: Entity] {

    val store = collection.mutable.Map[ID, A]()

    def create(entity: A): A = {
        entity.createdAt = now
        entity.modifiedAt = now
        store.put(entity.id, entity)
        entity
    }

    def update(entity: A): A = {
        entity.modifiedAt = now
        store.put(entity.id, entity)
        entity
    }

    def delete(entity: A): Unit = store.remove(entity.id)

    def find(id: ID): Option[A] = store.get(id)

    def findAll(): List[A] = store.values.toList
}
