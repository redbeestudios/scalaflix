package controllers

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import cats.syntax.all._
import models.FilmId
import play.api.mvc.{PathBindable, QueryStringBindable}

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
    * Path binder for [[models.FilmId]].
    *
    * @return A new Path binder for [[models.FilmId]].
    */
  implicit def filmIdPathBinder: PathBindable[FilmId] = new PathBindable[FilmId] {

    override def bind(key: String, value: String): Either[String, FilmId] =
      Try {
        FilmId(value.toLong)
      } fold (
        _ => Left("Invalid Film ID"),
        Right(_)
      )

    override def unbind(key: String, id: FilmId): String = id.value.toString

  }

}
