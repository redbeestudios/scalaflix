package converters.validation

import globals.{ApplicationResult, ValidationResult}

import scala.concurrent.Future

trait ValidationResultConverters {
  implicit class ValidationResultOps[+T](validationResult: ValidationResult[T]) {
    def toApplicationResult: ApplicationResult[T] = Future.successful(validationResult)
  }
}
