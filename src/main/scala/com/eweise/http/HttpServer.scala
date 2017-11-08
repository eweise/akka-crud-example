package com.eweise.http

import java.util.UUID

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{as, complete, entity, extractUri, get, path, post, _}
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.stream.ActorMaterializer
import com.eweise.domain.service.TaskService
import com.eweise.domain.{ErrorResponse, TaskRequest}
import com.eweise.Boot.taskRepo
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import io.circe.java8.time._
import io.circe.syntax._


class HttpServer(implicit val system:ActorSystem,
                 implicit val materializer: ActorMaterializer,
                 implicit val taskService: TaskService) {

    val log = Logging(system, this.getClass.getName)
    implicit val executionContext = system.dispatcher

    val route: Route =
        path("tasks") {
            get {
                complete(taskRepo.findAll().map(_.toTaskResponse))
            } ~ post {
                entity(as[TaskRequest]) { req => complete(taskService.create(UUID.randomUUID(),req))
                }
            }
        }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    implicit def defaultExceptionHandler = ExceptionHandler {
        case ex: Exception =>
            extractUri { uri =>
                log.error("unknown error", ex)
                val response = ErrorResponse(
                    StatusCodes.InternalServerError.intValue,
                    uri = uri.toString(),
                    ex.getClass.getName,
                    ex.getMessage).asJson.noSpaces

                complete(HttpResponse(StatusCodes.InternalServerError, entity = response))
            }
    }
}
