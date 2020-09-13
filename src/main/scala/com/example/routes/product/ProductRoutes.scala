package com.example.routes.product

import akka.http.scaladsl.server.Directives.{as, complete, entity, path}
import akka.http.scaladsl.server.Route
import com.example.routes.BaseRoute
import com.example.services.ServiceClass
import akka.http.scaladsl.server.Directives._

class ProductRoutes (serviceClass:ServiceClass) extends BaseRoute{

  private val createProductRoute =
    (path(productPathPrefix / "create") & post) {
      entity(as[String]) { json =>
        complete(serviceClass.createProduct(json))
      }
    }
  private val updateProductRoute =
    (path(productPathPrefix / "update") & put) {
      entity(as[String]) { json =>
        complete(serviceClass.updateProduct(json))
      }
    }
  val routes: Route =  createProductRoute ~ updateProductRoute
}

object ProductRoutes {
  def apply(): ProductRoutes = new ProductRoutes(ServiceClass())
}
