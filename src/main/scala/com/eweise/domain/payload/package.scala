package com.eweise.domain

import java.time.OffsetDateTime
import java.util.UUID

import akka.http.scaladsl.model.StatusCodes

package object payload {

    type Error = String

    type Errors = List[Error]


    final case class ErrorResponse(
                                          statusCode: Int,
                                          exceptionType: String,
                                          uri: String,
                                          message: String
                                  )

    final case class ValidationErrorResponse(
                                          statusCode: Int = StatusCodes.BadRequest.intValue,
                                          exceptionType: String = "ValidationError",
                                          uri: String,
                                          message: Errors
                                  )


    final case class TaskRequest(
                                        title: String,
                                        details: String,
                                        dueDate: Option[OffsetDateTime] = Some(OffsetDateTime.now()),
                                        complete: Option[Boolean] = Some(false)
                                )

    final case class TaskResponse(
                                         id: UUID,
                                         title: String,
                                         details: String,
                                         dueDate: OffsetDateTime,
                                         complete: Boolean
                                 )


}
