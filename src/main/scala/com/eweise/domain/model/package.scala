package com.eweise.domain

import java.time.OffsetDateTime
import java.util.UUID

import scalikejdbc._

package object model {
    type ID = UUID

    case class Person(id: UUID = UUID.randomUUID(),
                      username: String,
                      email: String,
                      password: String,
                      primaryAddress: Option[Address] = None,
                      primaryPhone: Option[String] = None,
                      primaryEmail: Option[String] = None,
                      createdAt: OffsetDateTime = OffsetDateTime.now,
                      modifiedAt: OffsetDateTime = OffsetDateTime.now
                     )


    case class Address(street1: String,
                       street2: Option[String],
                       city: String,
                       state: String,
                       postalCode: String)


    case class Task(id: ID = UUID.randomUUID(),
                    userId: ID,
                    title: String,
                    details: String,
                    dueDate: Option[OffsetDateTime] = None,
                    complete: Boolean = false,
                    createdAt: OffsetDateTime = OffsetDateTime.now,
                    modifiedAt: OffsetDateTime = OffsetDateTime.now
                   )


    object Task extends SQLSyntaxSupport[Task] {
        override val tableName = "task"

        def apply(t: ResultName[Task])(rs: WrappedResultSet) =
            new Task(
                id = UUID.fromString(rs.string(t.id)),
                userId = UUID.fromString(rs.string(t.userId)),
                title = rs.string(t.title),
                details = rs.string(t.details),
                dueDate = rs.offsetDateTimeOpt(t.dueDate),
                complete = rs.boolean(t.complete))
    }

}
