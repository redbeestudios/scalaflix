import error.ApplicationError

import scala.concurrent.Future

package object globals {
  type ValidationResult[+T] = Either[ApplicationError, T]
  type ApplicationResult[+T] = Future[ValidationResult[T]]
}
