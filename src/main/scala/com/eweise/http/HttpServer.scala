package com.eweise.http

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{as, complete, entity, extractUri, get, path, post, _}
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.stream.ActorMaterializer
import com.eweise.domain.service.TaskService
import com.eweise.domain.{ErrorResponse, TaskRequest}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import io.circe.java8.time._
import io.circe.syntax._

import scala.concurrent.Future


class HttpServer(implicit val system: ActorSystem,
                 implicit val taskService: TaskService) extends TimeInstances {

    val log = Logging(system, this.getClass.getName)

    val route: Route =
        pathPrefix(JavaUUID / "tasks") { userId =>
            pathEnd {
                get {
                    complete(taskService.findAll(userId))
                } ~ post {
                    entity(as[TaskRequest]) { req => complete(taskService.create(userId, req))
                    }
                }
            } ~ path(JavaUUID) {taskId =>
                 {
                    put {
                        entity(as[TaskRequest]) { req => complete(taskService.update(userId, taskId, req))
                        }
                    }
                }
            }
        }

    implicit def defaultExceptionHandler = ExceptionHandler {


        case ex: Exception =>
            extractUri { uri =>
                log.error("unknown error", ex)
                val response = ErrorResponse(
                    StatusCodes.InternalServerError.intValue,
                    ex.getClass.getName,
                    uri.toString(),
                    ex.getMessage).asJson.noSpaces

                complete(HttpResponse(StatusCodes.InternalServerError, entity = response))
            }
    }


    def start()(implicit materializer: ActorMaterializer): Future[ServerBinding] = Http().bindAndHandle(route, "localhost", 8080)

}
