package json

import cats.data.NonEmptyList
import controllers.circe.CirceImplicits
import error.ValidationError
import error.validation.ValidationErrorItem
import io.circe.Json
import io.circe.syntax._

/**
  * Trait that contains helper methods to create Validation errors.
  */
trait Validatable extends CirceImplicits {

  /**
    * Create an Invalid request and converts it to Json in order to handle Validation errors.
    *
    * @param validationErrors List of errors.
    * @return Json representation of the list of errors.
    */
  protected def validationErrorsAsInvalidRequestJson(validationErrors: NonEmptyList[ValidationErrorItem])
  : Json = validationErrorsAsInvalidRequest(validationErrors).asJson

  /**
    * Create an Invalid request in order to provide the Validation errors.
    *
    * @param validationErrors List of errors.
    * @return A [[error.ValidationError]] being the content of the invalid request.
    */
  protected def validationErrorsAsInvalidRequest(
      validationErrors: NonEmptyList[ValidationErrorItem],
    ): ValidationError = ValidationError(validationErrors)

}
