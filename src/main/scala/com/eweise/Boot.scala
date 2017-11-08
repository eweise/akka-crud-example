package com.eweise

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.eweise.domain.repository.TaskRepository
import com.eweise.domain.service.TaskService
import com.eweise.http.HttpServer
import com.typesafe.scalalogging.StrictLogging

object Boot extends App with StrictLogging {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    implicit val taskRepo = new TaskRepository()
    implicit val taskService = new TaskService()
    val httpServer = new HttpServer()
}
