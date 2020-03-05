package controllers.circe

import io.circe.generic.extras.semiauto._
import io.circe.generic.extras.{AutoDerivation, Configuration}
import io.circe.java8.time.{JavaTimeDecoders, JavaTimeEncoders}
import io.circe.{Decoder, Encoder, Printer}
import models.{FilmId, ViewId}

/**
  * Implicits circe json converters.
  */
trait CirceImplicits extends AutoDerivation with JavaTimeEncoders with JavaTimeDecoders {

  implicit val customPrinter: Printer = Printer.noSpaces.copy(dropNullValues = true)

  implicit val customConfig: Configuration = Configuration.default

  implicit val viewIdEncoder: Encoder[ViewId] = deriveUnwrappedEncoder
  implicit val viewIdDecoder: Decoder[ViewId] = deriveUnwrappedDecoder

  implicit val filmIdEncoder: Encoder[FilmId] = deriveUnwrappedEncoder
  implicit val filmIdDecoder: Decoder[FilmId] = deriveUnwrappedDecoder

}
