package com.example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContextExecutor

object RestServer {
  private final val HOST: String = "0.0.0.0"
  private final val PORT: Int = 9000

  def main(args: Array[String]): Unit = {

    implicit val actorSystem: ActorSystem = ActorSystem("HttpServer")

    implicit val fm: ActorMaterializer = ActorMaterializer()

    implicit val ec: ExecutionContextExecutor = actorSystem.dispatcher

    val httpRoute: Routes = new Routes()
    Http() bindAndHandle(httpRoute.routes, HOST, PORT)
  }
}
