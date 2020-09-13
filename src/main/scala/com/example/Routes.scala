package com.example

import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import com.example.routes.category.CategoryRoutes
import com.example.routes.product.ProductRoutes
import akka.http.scaladsl.server.Directives._
import scala.collection.immutable
import scala.concurrent.ExecutionContext

class Routes()(implicit val ec: ExecutionContext)  {

  val settings: CorsSettings = CorsSettings.defaultSettings.withAllowedMethods(immutable.Seq(GET, PUT, POST))

  val routes: Route = cors(settings) {
    CategoryRoutes().routes ~
      ProductRoutes().routes
  }
}

