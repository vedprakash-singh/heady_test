package com.example.mongo

import com.example.models.{SaveCategory, SaveProduct, SaveProductCategoryMapping, UpdateProductRequest}
import reactivemongo.bson.{BSONDocumentHandler, Macros}

object ImplicitHandler {
  implicit val productHandler: BSONDocumentHandler[SaveProduct] = Macros.handler[SaveProduct]
  implicit val categoryHandler: BSONDocumentHandler[SaveCategory] = Macros.handler[SaveCategory]
  implicit val updateProductRequestHandler: BSONDocumentHandler[UpdateProductRequest] = Macros.handler[UpdateProductRequest]
  UpdateProductRequest
  implicit val categoryProductHandler: BSONDocumentHandler[SaveProductCategoryMapping] = Macros.handler[SaveProductCategoryMapping]
}
