package com.eweise.domain

import java.time.OffsetDateTime
import java.util.UUID

package object model {
    type ID = UUID

    trait Entity {
        def id: ID
    }

    case class Task(id: ID = UUID.randomUUID(),
                    userId: ID,
                    title: String,
                    details: String,
                    dueDate: OffsetDateTime = OffsetDateTime.now(),
                    complete: Boolean = false
                   ) extends Entity {

        def toTaskResponse =
            TaskResponse(
                id = this.id,
                title = this.title,
                details = this.details,
                dueDate = this.dueDate,
                complete = this.complete)
    }

}
