package com.pm

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.pm.domain.repository.TaskRepository
import com.pm.domain.service.TaskService
import com.pm.http.HttpServer
import com.typesafe.scalalogging.StrictLogging

object Boot extends App with StrictLogging {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    implicit val taskRepo = new TaskRepository()
    implicit val taskService = new TaskService()
    val httpServer = new HttpServer()
}
