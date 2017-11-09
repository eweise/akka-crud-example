package com.eweise.domain

import java.time.OffsetDateTime
import java.util.UUID

import cats.data.Validated._
import com.eweise.domain.payload.{TaskRequest, TaskResponse}

package object model {
    type ID = UUID

    trait Entity {
        def id: ID
    }

    import cats.data.ValidatedNel
    import cats.implicits._

        val titleIsBlank: String = "Title is blank."

        val detailsIsBlank: String = "Details is blank."

    sealed trait FormValidatorNel {

        type ValidationResult[A] = ValidatedNel[String, A]

        private def validateTitle(title: String): ValidationResult[String] =
            if (title == null || title.isEmpty) title.validNel else titleIsBlank.invalidNel

        private def validateDetails(details: String): ValidationResult[String] =
            if (details == null || details.isEmpty) details.validNel else detailsIsBlank.invalidNel

        private def success[A](details: A): ValidationResult[A] = details.validNel

        def validateTaskRequest(req: TaskRequest): ValidationResult[TaskRequest] =
            (
                    validateTitle(req.title),
                    validateDetails(req.details),
                    success(req.dueDate),
                    success(req.complete)
            )
                    .mapN(TaskRequest)

    }

    object FormValidatorNel extends FormValidatorNel


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
