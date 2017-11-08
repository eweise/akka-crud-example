package com.pm.domain

import java.time.OffsetDateTime
import java.util.UUID

final case class ErrorResponse(
                                      statusCode: Int,
                                      uri: String,
                                      exceptionType: String,
                                      message: String
                              )

final case class TaskRequest(
                                    title: String,
                                    details: String,
                                    dueDate: Option[OffsetDateTime] ,
                                    complete: Option[Boolean]
                            )

final case class TaskResponse(
                                     id: UUID,
                                     title: String,
                                     details: String,
                                     dueDate: OffsetDateTime,
                                     complete: Boolean
                             )

