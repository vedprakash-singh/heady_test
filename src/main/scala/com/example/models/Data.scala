package com.example.models

import reactivemongo.bson.Macros.Annotations.Key

sealed trait Data

case class CreateCategoryRequest(id: Option[String],
                                 name: String,
                                 parentId: Option[String],
                                 productId: Option[List[String]]
                                )

case class CreateProductRequest(id: Option[String],
                                name: String,
                                price: Option[Double],
                                categoryId: Option[String]
                               )

case class UpdateProductRequest(id: String,
                                name: Option[String],
                                price: Option[Double],
                                categoryId: Option[String]
                               )

case class SaveCategory(@Key("_id") id: String,
                        name: String,
                        parentId: Option[String],
                        productId: Option[List[String]]) extends Data

case class SaveProduct(@Key("_id") id: String,
                       name: String,
                       price: Option[Double],
                       categoryId: Option[String]) extends Data

case class SaveProductCategoryMapping(@Key("_id") id: String,
                                      catId: String,
                                      productId: String
                                     ) extends Data