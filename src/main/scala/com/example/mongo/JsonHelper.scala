package com.example.mongo

import org.json4s.native.JsonMethods.{parse => jParser}
import org.json4s.native.Serialization.{write => jWrite}
import org.json4s.{DefaultFormats, JValue}

object JsonHelper {

  implicit val formats: DefaultFormats = DefaultFormats

  def write[T <: AnyRef](value: T): String = jWrite(value)

  def parse(value: String): JValue = jParser(value)

}
