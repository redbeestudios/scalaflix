package converters.validation

import cats.data.ValidatedNel
import error.ValidationError
import error.validation.ValidationErrorItem
import globals.ValidationResult

trait ValidationErrorItemsConverters {
  implicit class ValidationErrorItemsNelOps(validatedNel: ValidatedNel[ValidationErrorItem, Unit]) {
    def toValidationResult[T](value: T): ValidationResult[T] = validatedNel
      .leftMap(ValidationError).toEither.map(_ => value)
  }
}
