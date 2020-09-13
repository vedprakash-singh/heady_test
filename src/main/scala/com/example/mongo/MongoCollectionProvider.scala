package com.example.mongo

import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.{DefaultDB, MongoConnection, MongoDriver}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.util.control.NonFatal
import scala.util.{Failure, Success}

private[mongo] object MongoCollectionProvider {
  lazy val dataBaseSystem: DefaultDB = {
    val host = "localhost"
    val port = "27017"
    val dbName = "local_db"
    val uri = s"mongodb://$host:$port/$dbName"

    try {
      val driver = MongoDriver()
      val parsedUri = MongoConnection.parseURI(uri)
      parsedUri.map(driver.connection) match {
        case Success(value) =>
          val result = Await.result(value.database(dbName), 100 seconds)
          result
        case Failure(th) =>
          throw th
      }
    }
    catch {
      case NonFatal(th) =>
        th.printStackTrace()
        throw th
    }
  }
}

trait MongoCollectionProvider {
  val collectionName: String

  def collection: BSONCollection =
    MongoCollectionProvider.dataBaseSystem.collection(collectionName)
}
