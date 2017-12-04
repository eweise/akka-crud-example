package com.eweise.domain

import java.time.OffsetDateTime
import java.util.UUID

final case class ErrorResponse(statusCode: Int,
                               uri: String,
                               exceptionType: String,
                               message: String)

final case class LoginRequest(email: String, password: String)

final case class LoginResponse(username: String, token: String)

final case class RegistrationRequest(username: String, password: String, email: String)

final case class RegistrationResponse(token: String)

final case class TaskRequest(title: String,
                             details: String,
                             dueDate: Option[OffsetDateTime] = None,
                             complete: Option[Boolean] = Some(false))

final case class TaskResponse(id: UUID,
                              title: String,
                              details: String,
                              dueDate: Option[OffsetDateTime] = None,
                              complete: Boolean)
