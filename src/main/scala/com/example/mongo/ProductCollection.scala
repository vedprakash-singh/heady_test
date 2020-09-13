package com.example.mongo

import com.example.models.{SaveProduct, UpdateProductRequest}
import com.example.mongo.ImplicitHandler._
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONDocumentReader}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.control.NonFatal

trait ProductCollection {
  x: MongoCollectionProvider =>
  def create(product: SaveProduct): Future[WriteResult] = {
    collection.insert(product)
  }

  def getProductsById(ids: List[String]): Future[List[SaveProduct]] = {
    Future.sequence(ids.map(id => collection.find(BSONDocument("_id" -> id)).requireOne[SaveProduct]))
  }

  def update(updateProductRequest: UpdateProductRequest) = {
    collection.update(ordered = false)
      .one(
        q = BSONDocument("_id" -> updateProductRequest.id),
        u = updateProductRequest,
        upsert = false,
        multi = false
      ).map(_.n)
  }

  def exist(id: String)(implicit reader: BSONDocumentReader[SaveProduct]): Future[Boolean] = collection.find(BSONDocument("_id" -> id)).requireOne[SaveProduct].map {
    _ => true
  }.recover {
    case NonFatal(_) => false
  }
}

object ProductCollection extends ProductCollection with MongoCollectionProvider {
  override val collectionName: String = "products"
}
