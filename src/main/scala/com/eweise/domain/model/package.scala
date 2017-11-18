package com.eweise.domain

import java.time.OffsetDateTime
import java.util.UUID

import scalikejdbc._

package object model {
    type ID = UUID

    trait Entity {
        var createdAt: OffsetDateTime = OffsetDateTime.now
        var modifiedAt: OffsetDateTime = OffsetDateTime.now

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


    object Task extends SQLSyntaxSupport[Task] {
        override val tableName = "task"

        def apply(t: ResultName[Task])(rs: WrappedResultSet) =
            new Task(
                id = UUID.fromString(rs.string(t.id)),
                userId = UUID.randomUUID(),
                title = rs.string(t.title),
                details = rs.string(t.details),
                dueDate = rs.offsetDateTimeOpt(t.dueDate),
                complete = rs.boolean(t.complete))
    }

}
