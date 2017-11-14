package com.eweise.domain.payload

import java.time.OffsetDateTime
import java.util.UUID

import com.eweise.domain.{TaskRequest, TaskResponse}

trait PayloadFixture {

    def createTaskRequest() = TaskRequest(title = "t2", details = "d2")

    def createTaskResponse() = TaskResponse(
        id = UUID.randomUUID,
        title = "t1",
        details = "d1",
        dueDate = Some(OffsetDateTime.now()),
        complete = true)
}
