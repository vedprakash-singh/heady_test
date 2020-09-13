package com.example.routes

import akka.http.scaladsl.server.PathMatcher
import akka.http.scaladsl.server.Directives._
trait BaseRoute {
  private val basePath = "heady" / "v1"
  val categoryPathPrefix: PathMatcher[Unit] = basePath / "category"
  val productPathPrefix: PathMatcher[Unit] = basePath / "product"

}
