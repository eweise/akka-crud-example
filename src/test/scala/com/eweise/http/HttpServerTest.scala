package com.eweise.http

import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.eweise.domain.TaskResponse
import com.eweise.domain.payload.PayloadFixture
import com.eweise.domain.service.TaskService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import io.circe.java8.time._
import io.circe.syntax._
import org.mockito.Matchers.{any, refEq}
import org.mockito.Mockito
import org.scalatest.{Matchers, WordSpec}

import scala.util.Success

class HttpServerTest extends WordSpec with Matchers with ScalatestRouteTest with PayloadFixture with TimeInstances {
    implicit val mockTaskServer = Mockito.mock(classOf[TaskService])
    val httpServer = new HttpServer()

    "HttpServer" should {
        "create a task" in {
            val taskRequest = createTaskRequest()
            val taskResponse = createTaskResponse()

            Mockito.when(mockTaskServer.create(any(), refEq(taskRequest))).thenReturn(Success(taskResponse))

            Post("/tasks", HttpEntity(`application/json`, taskRequest.asJson.noSpaces)) ~> httpServer.route ~> check {
                responseAs[TaskResponse] shouldEqual taskResponse
            }
        }

        "get tasks" in {
            val tasks = List(createTaskResponse(), createTaskResponse())
            Mockito.when(mockTaskServer.findAll(any())).thenReturn(Success(tasks))

            Get("/tasks") ~> httpServer.route ~> check {
                responseAs[List[TaskResponse]] shouldEqual tasks
            }
        }
    }
}