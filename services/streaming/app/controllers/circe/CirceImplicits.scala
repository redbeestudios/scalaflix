package controllers.circe

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

import domain.Genre
import io.circe.{Decoder, Encoder, Printer}

import scala.util.Try

/**
  * Circe implicits.
  */
trait CirceImplicits {

  implicit val customPrinter: Printer = Printer.noSpaces.copy(dropNullValues = true)

  implicit val localDateTimeEncoder: Encoder[LocalDateTime] =
    Encoder[String].contramap(_.truncatedTo(ChronoUnit.SECONDS).toString)

  implicit val genreEncoder: Encoder[Genre] = Encoder[String].contramap(_.value)
  implicit val genreDecoder: Decoder[Genre] = Decoder[String].emapTry { value =>
    Try(Genre(value = value))
  }

}
