package com.eweise.intf

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.eweise.domain.payload.PayloadFixture
import io.circe.java8.time._
import org.scalatest.{Matchers, WordSpec}

class HttpServerTest extends WordSpec with Matchers with ScalatestRouteTest with PayloadFixture with TimeInstances {
    //    implicit val mockTaskServer = Mockito.mock(classOf[TaskServiceTest])
    //    val httpServer = new HttpServer()

    //    "HttpServer" should {
    //        "create a task" in {
    //            val taskRequest = createTaskRequest()
    //            val taskResponse = createTaskResponse()
    //
    //            Mockito.when(mockTaskServer.create(any(), refEq(taskRequest))).thenReturn(taskResponse)
    //
    //            Post("/tasks", HttpEntity(`application/json`, taskRequest.asJson.noSpaces)) ~> httpServer.route ~> check {
    //                responseAs[TaskResponse] shouldEqual taskResponse
    //            }
    //        }
    //
    //        "get tasks" in {
    //            val tasks = List(createTaskResponse(), createTaskResponse())
    //            Mockito.when(mockTaskServer.findAll(any())).thenReturn(tasks)
    //
    //            Get("/tasks") ~> httpServer.route ~> check {
    //                responseAs[List[TaskResponse]] shouldEqual tasks
    //            }
    //        }
    //    }
}