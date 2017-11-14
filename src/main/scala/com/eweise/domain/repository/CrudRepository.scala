package com.eweise.domain.repository

import com.eweise.domain.model.{Entity, ID}

import scala.util.{Success, Try}

trait CrudRepository[A <: Entity] {

    val store = collection.mutable.Map[ID, A]()

    def create(entity: A): Try[A] = {
        store.put(entity.id, entity)
        Success(entity)
    }

    def update(entity: A): Try[A] = {
        store.put(entity.id, entity)
        Success(entity)
    }

    def delete(entity: A): Try[Unit] = Success(store.remove(entity.id))

    def find(id: ID): Try[Option[A]] = Success(store.get(id))

    def findAll(): Try[List[A]] = Success(store.values.toList)
}
