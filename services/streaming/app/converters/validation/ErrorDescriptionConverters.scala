package converters.validation

import cats.data.NonEmptyList
import error.validation.{ValidationErrorItem, ValidationErrorItemType}

trait ErrorDescriptionConverters {

  implicit class ErrorDescriptionOps(descriptions: NonEmptyList[String]) {
    def toValidationErrorItems(code: ValidationErrorItemType, param: String): NonEmptyList[ValidationErrorItem] = {
      descriptions.map(description => ValidationErrorItem(code, param, description))
    }
  }

}
