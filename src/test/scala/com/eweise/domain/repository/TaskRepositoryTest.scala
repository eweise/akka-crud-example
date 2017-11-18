package com.eweise.domain.repository

import java.util.UUID

import com.eweise.domain.model.Task
import com.eweise.test.{DBTestBoot, DBTestSupport}
import org.scalatest.{FlatSpec, Matchers}

class TaskRepositoryTest extends FlatSpec with Matchers with DBTestBoot with DBTestSupport {

    "create" should "save to db" in {
        val taskRepository = new TaskRepository()
        val userId = UUID.randomUUID()

        autoRollback { implicit session =>
            val newTask = taskRepository.create(Task(userId = userId, title = "A", details = "B"))
            val response = taskRepository.find(newTask.id)
            response should not be None
        }
    }
}
