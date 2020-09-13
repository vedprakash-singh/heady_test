
package com.example.mongo

import com.example.models.SaveCategory
import com.example.mongo.ImplicitHandler._
import reactivemongo.api.Cursor
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait CategoryCollection {
  x: MongoCollectionProvider =>
  def create(product: SaveCategory): Future[WriteResult] = {
    collection.insert(product)
  }

  def getByParentId(id: String): Future[List[SaveCategory]] = {
    collection.find(BSONDocument("parentId" -> id)).cursor[SaveCategory]()
      .collect[List](-1, Cursor.FailOnError[List[SaveCategory]]())

  }

  def get(id: String): Future[SaveCategory] = {
    collection.find(BSONDocument("_id" -> id)).requireOne[SaveCategory]
  }

}

object CategoryCollection extends CategoryCollection with MongoCollectionProvider {
  override val collectionName: String = "category"
}