package com.eweise

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.eweise.domain.repository.{PersonRepository, TaskRepository}
import com.eweise.domain.service.{PersonService, TaskService, WebToken}
import com.eweise.intf.{Database, HttpServer, Migrator}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging

import scala.io.StdIn

object Boot extends App with StrictLogging {
    lazy implicit val system = ActorSystem()
    lazy implicit val materializer = ActorMaterializer()
    lazy implicit val executionContext = system.dispatcher

    lazy implicit val webToken = new WebToken()

    lazy implicit val personRepo = new PersonRepository()
    lazy implicit val taskRepo = new TaskRepository()
    lazy implicit val taskService = new TaskService()
    lazy implicit val personService = new PersonService()


    val config = ConfigFactory.load()
    val dbConfig = config.getConfig("database")
    val dbPoolConfiguration = new Database(dbConfig)
    val serverBinding = new HttpServer().start()

    println("migrating...")
    new Migrator(dbConfig).flyway

    println("server is ready")
    StdIn.readLine()
    // Unbind from the port and shut down when done
    serverBinding
            .flatMap(_.unbind())
            .onComplete(_ => system.terminate())
}
