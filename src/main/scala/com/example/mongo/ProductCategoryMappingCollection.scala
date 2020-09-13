
package com.example.mongo

import com.example.models.SaveProductCategoryMapping
import com.example.mongo.ImplicitHandler._
import reactivemongo.api.Cursor
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait ProductCategoryMappingCollection {
  x: MongoCollectionProvider =>
  def create(product: SaveProductCategoryMapping): Future[WriteResult] = {
    collection.insert(product)
  }

  def getByCategoryId(categoryId: String): Future[List[SaveProductCategoryMapping]] = {
    collection.find(BSONDocument("catId" -> categoryId)).cursor[SaveProductCategoryMapping]()
      .collect[List](-1, Cursor.FailOnError[List[SaveProductCategoryMapping]]())

  }

}

object ProductCategoryMappingCollection extends ProductCategoryMappingCollection with MongoCollectionProvider {
  override val collectionName: String = "productCategoryMapping"
}
