package com.eweise.domain.repository

import java.util.UUID

import com.eweise.domain.model.Task
import com.eweise.intf.DBTestSupport
import org.scalatest.{FlatSpec, Matchers}

class TaskRepositoryTest extends FlatSpec with Matchers with DBTestSupport {

    "create update and find " should "save to db and find" in {
        val taskRepository = new TaskRepository()
        val userId = UUID.randomUUID()

        autoRollback { implicit session =>
            val newTask = taskRepository.create(userId, Task(userId = userId, title = "A", details = "B"))
            val response = taskRepository.find(userId, newTask.id)

            response should not be None

            taskRepository.update(userId, response.get.copy(details = "C"))

            val responseAfterUpdate = taskRepository.find(userId, newTask.id)

            responseAfterUpdate should not be None

            val deleteResult = taskRepository.delete(userId, responseAfterUpdate.get.id)

            deleteResult shouldBe  1

            taskRepository.findAll(userId).length shouldEqual 0
        }
    }
}
