package controllers.circe

import domain.Genre
import io.circe.{ Decoder, Encoder, Printer }

import scala.util.Try

/**
 * Circe implicits.
 */
trait CirceImplicits {

  implicit val customPrinter: Printer = Printer.noSpaces.copy(dropNullValues = true)

  implicit val genreEncoder: Encoder[Genre] = Encoder[String].contramap(_.name)
  implicit val genreDecoder: Decoder[Genre] = Decoder[String].emapTry { genre =>
    Try(Genre(genre))
  }

}
