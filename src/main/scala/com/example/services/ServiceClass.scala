package com.example.services

import java.util.UUID

import com.example.models._
import com.example.mongo.ImplicitHandler._
import com.example.mongo._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ServiceClass(productCollection: ProductCollection,
                   categoryCollection: CategoryCollection,
                   productCategoryMappingCollection: ProductCategoryMappingCollection) extends JsonParser {

  /**
   * Create Product
   *
   * @param json
   * @return
   */
  def createProduct(json: String): Future[String] = {
    parseJson[CreateProductRequest](json) match {
      case Left(exceptionResponse) => Future.successful(JsonHelper.write(exceptionResponse))
      case Right(createProductRequest) =>
        productCollection.exist(createProductRequest.id.getOrElse("")).flatMap {
          case false =>
            val id = java.util.UUID.randomUUID().toString.replaceAll("-", "")
            val data = SaveProduct(id, createProductRequest.name, createProductRequest.price, createProductRequest.categoryId)
            productCollection.create(data).flatMap(x =>
              productCategoryMappingCollection.create(SaveProductCategoryMapping(UUID.randomUUID().toString,
                createProductRequest.categoryId.getOrElse(""), id)))
            Future.successful(id)
          case true => Future.successful(JsonHelper.write("Product Already exists"))
        }
    }
  }

  /**
   * Create Product
   *
   * @param json
   * @return
   */
  def updateProduct(json: String): Future[String] = {
    parseJson[UpdateProductRequest](json) match {
      case Left(exceptionResponse) =>
        Future.successful(JsonHelper.write(exceptionResponse))
      case Right(updateProductRequest) =>
        productCollection.update(updateProductRequest).map {
          x =>
            if (x != 0) {
              updateProductRequest.id
            }
            else {
              JsonHelper.write("Unbale to update product")
            }
        }

    }
  }

  /**
   * getAll Product By CategoryId
   *
   * @param categoryId
   * @return
   */
  def getAllProductsByCategory(categoryId: String) = {
    for {x <- productCategoryMappingCollection.getByCategoryId(categoryId).recover {
      case _ => List(SaveProductCategoryMapping("", "", ""))
    }
      .flatMap(productCategory => productCollection.getProductsById(productCategory.map(_.productId)))

         } yield {
      JsonHelper.write(x)
    }
  }

  /**
   * Get All categories
   *
   * @param id
   * @return
   */
  def getAllCategories(id: String) = {
    for {
      x <- categoryCollection.get(id).recover {
        case _ => SaveCategory("", "", None, None)
      }
        .flatMap { category =>
          getSubCategories(List(category), List())
        }

    } yield {
      JsonHelper.write(x)
    }
  }

  /**
   * Create Category
   *
   * @param json
   * @return
   */
  def createCategory(json: String): Future[String] = {
    parseJson[CreateCategoryRequest](json) match {
      case Left(exceptionResponse) => Future.successful(JsonHelper.write(exceptionResponse))
      case Right(createCategoryRequest) =>
        productCollection.exist(createCategoryRequest.id.getOrElse("")).flatMap {
          case false =>
            val id = java.util.UUID.randomUUID().toString.replaceAll("-", "")
            val data = SaveCategory(id, createCategoryRequest.name, createCategoryRequest.parentId, createCategoryRequest.productId)
            categoryCollection.create(data).flatMap { x =>
              Future.sequence(
                createCategoryRequest.productId.getOrElse(List()).map { productId =>
                  productCategoryMappingCollection.create(SaveProductCategoryMapping(UUID.randomUUID().toString,
                    id, productId))
                }
              )
            }
            Future.successful(id)
          case true => Future.successful(JsonHelper.write("Category Already exists"))
        }
    }
  }

  /**
   * Get SubCategory
   *
   * @param categories
   * @param categoryResponse
   * @return
   */
  private def getSubCategories(categories: List[SaveCategory], categoryResponse: List[SaveCategory])
  : Future[List[SaveCategory]] = {
    if (categories.isEmpty) {
      Future.successful(categoryResponse)
    }
    else {
      val categoriesData = categories.map {
        category =>
          categoryCollection.get(category.parentId.getOrElse("")).map {
            parent => category.copy(parentId = Some(parent.id))
          }.recover {
            case _ => category.copy(parentId = Some(""))
          }
      }
      (for {
        categoryList <- Future.sequence(categoriesData)
        subCategories <- Future.sequence(categoryList.map { category =>
          categoryCollection.getByParentId(category.id).recover { case _ => List(SaveCategory("", "", None, None)) }
        }).map(_.flatten)
      } yield {
        getSubCategories(subCategories, categoryList ::: categoryResponse)
      }).flatMap(identity)
    }
  }
}

object ServiceClass {
  def apply(): ServiceClass = new ServiceClass(
    ProductCollection,
    CategoryCollection,
    ProductCategoryMappingCollection)
}
