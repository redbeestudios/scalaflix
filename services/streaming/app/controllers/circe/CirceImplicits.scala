package controllers.circe

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

import com.google.common.base.CaseFormat
import domain.Genre
import error.validation.ValidationErrorItemType
import io.circe.generic.AutoDerivation
import io.circe.{Decoder, Encoder, Printer}

import scala.util.Try

/**
  * Circe implicits.
  */
trait CirceImplicits extends AutoDerivation {

  implicit val customPrinter: Printer = Printer.noSpaces.copy(dropNullValues = true)

  implicit val localDateTimeEncoder: Encoder[LocalDateTime] =
    Encoder[String].contramap(_.truncatedTo(ChronoUnit.SECONDS).toString)

  implicit val genreEncoder: Encoder[Genre] = Encoder[String].contramap(_.value)
  implicit val genreDecoder: Decoder[Genre] = Decoder[String].emapTry { value =>
    Try(Genre(value = value))
  }

  implicit val validationErrorItemTypeEncoder: Encoder[ValidationErrorItemType] = Encoder[String]
    .contramap { validationErrorItemType =>
      CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, validationErrorItemType.toString)
    }
  implicit val validationErrorItemTypeDecoder: Decoder[ValidationErrorItemType] = Decoder[String]
    .emapTry { validationErrorItemType =>
      Try(ValidationErrorItemType(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, validationErrorItemType)))
    }
}
