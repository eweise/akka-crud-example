package com.eweise.intf

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{as, complete, entity, extractUri, get, path, post, _}
import akka.http.scaladsl.server.{Directive1, ExceptionHandler, Route}
import akka.stream.ActorMaterializer
import com.eweise.domain.service.{TaskService, WebToken}
import com.eweise.domain.{ErrorResponse, NotFoundException, TaskRequest}
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
            } ~ path(JavaUUID) { taskId => {
                put {
                    entity(as[TaskRequest]) { req => complete(taskService.update(userId, taskId, req))
                    }
                }
            }
            } ~ path(JavaUUID) { taskId =>
                complete(taskService.find(userId, taskId))
            }
        }

    implicit def defaultExceptionHandler = ExceptionHandler {
        case notFound: NotFoundException =>
            extractUri { uri =>
                log.error("not Found error", notFound)
                val response = ErrorResponse(
                    StatusCodes.NotFound.intValue,
                    uri.toString(),
                    notFound.getClass.getName,
                    notFound.getMessage).asJson.noSpaces

                complete(HttpResponse(StatusCodes.InternalServerError, entity = response))
            }
        case ex: Exception =>
            extractUri { uri =>
                log.error("unknown error", ex)
                val response = ErrorResponse(
                    StatusCodes.InternalServerError.intValue,
                    uri.toString(),
                    ex.getClass.getName,
                    ex.getMessage).asJson.noSpaces

                complete(HttpResponse(StatusCodes.InternalServerError, entity = response))
            }
    }

//    private def authenticated: Directive1[Map[String, Any]] =
//        optionalHeaderValueByName("Authorization").flatMap {
//            case Some(jwt) if isTokenExpired(jwt) =>
//                complete(StatusCodes.Unauthorized -> "Token expired.")
//
//            case Some(jwt) if WebToken.decode(jwt) =>
//                provide(getClaims(jwt).getOrElse(Map.empty[String, Any]))
//
//            case _ => complete(StatusCodes.Unauthorized)
//        }


    def start()(implicit materializer: ActorMaterializer): Future[ServerBinding] =
        Http().bindAndHandle(route, "localhost", 8080)


}
