package com.example.mongo

import org.json4s.DefaultFormats

import scala.util.control.NonFatal

trait JsonParser {

  implicit val formats: DefaultFormats = DefaultFormats


  /**
   * Parsing JSON string to the specified case class.
   *
   * @param json
   * @tparam M
   * @return $Either[ResponseHeader, M]
   */
  def parseJson[M](json: String)(implicit m: Manifest[M]): Either[ErrorResponse, M] = {
    try
      Right(JsonHelper.parse(json).extract[M])
    catch {
      case NonFatal(th) =>
        th.getCause.getCause match {
          case e: java.lang.IllegalArgumentException =>
            println(s"Invalid JSON - $json", e)
            Left(handle(exception = EmptyFieldException(e.getMessage.split(":").last)))
          case _ =>
            Left(handle(exception = new IllegalArgumentException("Invalid Json", th)))
        }
    }
  }

  def handle(exception: Throwable): ErrorResponse = ErrorResponse(ResponseHeader("", "", "", "", ResponseError("", "")))

}

case class ResponseError(
                          statusCode: String,
                          message: String
                        )

case class ResponseHeader(
                           requestId: String,
                           responseId: String,
                           status: String,
                           message: String,
                           error: ResponseError
                         )

case class ErrorResponse(responseHeader: ResponseHeader)

case class EmptyFieldException(message: String) extends IllegalArgumentException(message)