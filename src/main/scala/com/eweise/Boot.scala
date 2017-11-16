package com.eweise

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.eweise.domain.repository.TaskRepository
import com.eweise.domain.service.TaskService
import com.eweise.http.HttpServer
import com.typesafe.scalalogging.StrictLogging

import scala.io.StdIn

object Boot extends App with StrictLogging {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    implicit val taskRepo = new TaskRepository()
    implicit val taskService = new TaskService()

//    Migrate.flyway

    val serverBinding = new HttpServer().start()

    StdIn.readLine()
    // Unbind from the port and shut down when done
    serverBinding
            .flatMap(_.unbind())
            .onComplete(_ => system.terminate())
}
