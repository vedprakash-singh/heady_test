package com.example.routes.category

import com.example.routes.BaseRoute
import com.example.services.ServiceClass
import akka.http.scaladsl.server.Directives.{as, complete, entity, path, _}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class CategoryRoutes (serviceClass:ServiceClass) extends BaseRoute{

  private val createCategoryRoute =
    (path(categoryPathPrefix /"create") & post) {
      entity(as[String]) { json =>
        complete(serviceClass.createCategory(json))
      }
    }

  private val getAllCategoryRoute =
    (path(categoryPathPrefix /"getAll"/ Segment) & get) { categoryId => {
        complete(serviceClass.getAllCategories(categoryId))
      }
    }

  private val getAllProductsByCategoryRoute =
    (path(categoryPathPrefix /"getAllProductsByCategory"/ Segment) & get) { categoryId => {
      complete(serviceClass.getAllProductsByCategory(categoryId))
    }
    }


  val routes: Route = createCategoryRoute ~
    getAllCategoryRoute ~ getAllProductsByCategoryRoute
}

object CategoryRoutes {
  def apply(): CategoryRoutes = new CategoryRoutes(ServiceClass())
}