package controllers

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import cats.syntax.all._
import play.api.mvc.QueryStringBindable

import scala.util.Try

/**
  * Binders for path and query params.
  */
package object binders {

  /**
    * Query String binder for [[java.time.LocalDateTime]].
    *
    * @return A new Query String binder for [[java.time.LocalDateTime]].
    */
  implicit def localDateTimeQueryStringBinder: QueryStringBindable[LocalDateTime] =
    new QueryStringBindable[LocalDateTime] {

      private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

      override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, LocalDateTime]] =
        params.get(key) map { value =>
          Try(LocalDateTime.parse(value.head, this.formatter)).toEither.leftMap(_ => s"Invalid parameter: $key")
        }

      override def unbind(key: String, value: LocalDateTime): String =
        this.formatter.format(value)

    }

  /**
    * genres binder
    *
    * @param key key
    * @param stringBinder binder
    * @return bound genres
    */
  implicit def genreBinder(
      key: String
    )(implicit stringBinder: QueryStringBindable[String]
    ): QueryStringBindable[List[String]] =
    new QueryStringBindable[List[String]] {
      override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, List[String]]] =
        stringBinder.bind(key, params).map(_.right.map(_.split(",").toList))

      override def unbind(key: String, strings: List[String]): String =
        s"""$key=${strings.mkString(",")}"""
    }
}
