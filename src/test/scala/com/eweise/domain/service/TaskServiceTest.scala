package com.eweise.domain.service

import java.util.UUID

import com.eweise.domain.TaskRequest
import com.eweise.domain.repository.TaskRepository
import com.eweise.intf.Database
import com.typesafe.config.ConfigFactory
import org.scalatest.{FlatSpec, Matchers}


class TaskServiceTest extends FlatSpec with Matchers {

    "create" should "save to db" in {

        val config = ConfigFactory.load()
        new Database(config.getConfig("database"))
        implicit val taskRepository = new TaskRepository()
        val taskService = new TaskService()
        val userId = UUID.randomUUID()
        val newTask = taskService.create(userId, TaskRequest("A","B") )

        val response = taskService.find(userId, newTask.id)
        response should not be None
    }
}
