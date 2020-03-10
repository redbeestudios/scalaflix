package error

import cats.data.NonEmptyList
import error.validation.ValidationErrorItem

case class ValidationError(validationErrorItems: NonEmptyList[ValidationErrorItem]) extends ApplicationError
