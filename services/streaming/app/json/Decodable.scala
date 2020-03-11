package json

import cats.data.NonEmptyList
import error.validation.{InvalidParam, ParamRequired, ValidationErrorItem}
import io.circe._
import play.api.Logging
import play.api.libs.circe.Circe
import play.api.mvc.{BaseController, BodyParser, Result, Results}

import scala.concurrent.ExecutionContext

/**
  * Custom body parser for validations.
  */
trait Decodable extends Circe with Writeable
  with Validatable with Logging with Results with Readable { _: BaseController =>

  /**
    * Body parser for json applying validations.
    *
    * @param ec An implicit execution context.
    * @tparam T A circe decoder.
    * @return A body parser.
    */
  protected def decode[T: Decoder](implicit ec: ExecutionContext): BodyParser[T] =
    circe.json.validate(decodeJsonValue[T])

  private def decodeJsonValue[T: Decoder](json: Json): Either[Result, T] =
    implicitly[Decoder[T]]
      .decodeAccumulating(json.hcursor)
      .leftMap { ex =>
        val validationErrorItems = NonEmptyList.fromListUnsafe(decodingFailuresAsValidationError(ex))
        val json = validationErrorsAsInvalidRequestJson(validationErrorItems)
        logger.error(s"Error decoding request. Invalid body:\n$json")
        BadRequest(json)
      }
      .toEither

  private def decodingFailuresAsValidationError(
      decodingFailures: NonEmptyList[DecodingFailure]
    ): List[ValidationErrorItem] =
    decodingFailures.filterNot(_.message.contains("[A]Option[A]")) map { failure =>
      val possiblePath              = CursorOp.opsToPath(failure.history)
      val path: String              = if (failure.history.isEmpty) possiblePath else possiblePath.substring(1)
      val requiredFieldErrorMessage = "required:"
      failure.message match {
        case "Attempt to decode value on failed cursor" =>
          ValidationErrorItem(ParamRequired, path, "Could not find param.")

        case message if message.startsWith(requiredFieldErrorMessage) =>
          val field = message.drop(requiredFieldErrorMessage.length)
          ValidationErrorItem(ParamRequired, field.toString, "Could not find param")

        case message =>
          val fieldErrorMessage = "field:"
          val field =
            if (message.startsWith(fieldErrorMessage)) message.drop(fieldErrorMessage.length)
            else path

          ValidationErrorItem(InvalidParam, field, "Invalid parameter")
      }
    } distinct

}
