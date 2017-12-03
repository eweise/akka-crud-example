package com.eweise.intf

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{as, complete, entity, extractUri, get, path, post, _}
import akka.http.scaladsl.server.directives.Credentials
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.stream.ActorMaterializer
import com.eweise.domain.service.{JwtToken, PersonService, TaskService, UserClaim}
import com.eweise.domain.{ErrorResponse, LoginRequest, NotFoundException, RegistrationRequest, TaskRequest, ValidationFailedException}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import io.circe.java8.time._
import io.circe.syntax._

import scala.concurrent.Future


class HttpServer(implicit val system: ActorSystem,
                 implicit val taskService: TaskService,
                 implicit val personService: PersonService,
                implicit val jwtToken : JwtToken ) extends TimeInstances {

    val log = Logging(system, this.getClass.getName)

    val route: Route =
        authenticateOAuth2(realm = "secure site", myUserPassAuthenticator) { userName =>
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
        } ~ pathPrefix("users") {
            path("login") {
                pathEnd {
                    post {
                        entity(as[LoginRequest]) { req => complete(personService.login(req)) }
                    }
                }
            } ~ pathEnd {
                post {
                    entity(as[RegistrationRequest]) { req => complete(personService.register(req)) }
                }
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
        case validationEx: ValidationFailedException =>
            extractUri { uri =>
                val response = ErrorResponse(
                    StatusCodes.BadRequest.intValue,
                    uri.toString(),
                    validationEx.getClass.getName,
                    validationEx.errors.asJson.noSpaces).asJson.noSpaces

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

    def myUserPassAuthenticator(credentials: Credentials): Option[String] =
        credentials match {
            case p@Credentials.Provided(id)  => {
               val result = jwtToken.find(id)
                println(result)
                None
            }
            case _ => None
        }

    def start()(implicit materializer: ActorMaterializer): Future[ServerBinding] =
        Http().bindAndHandle(route, "localhost", 8080)
}
