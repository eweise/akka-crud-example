package com.eweise.domain

import java.time.OffsetDateTime
import java.util.UUID

package object model {
    type ID = UUID

    trait Entity {
        var createdAt:OffsetDateTime = OffsetDateTime.now
        var updatedAt:OffsetDateTime = OffsetDateTime.now

        def id: ID
    }

    case class User(id: UUID = UUID.randomUUID(),
                    username: String,
                    email: String,
                    password: String
                   ) extends Entity

    case class Task(id: ID = UUID.randomUUID(),
                    userId: ID,
                    title: String,
                    details: String,
                    dueDate: Option[OffsetDateTime] = None,
                    complete: Boolean = false
                   ) extends Entity {
    }

}
