import error.ApplicationError
import play.api.libs.Files
import play.api.mvc.MultipartFormData

import scala.concurrent.Future

package object globals {
  type ValidationResult[+T]  = Either[ApplicationError, T]
  type ApplicationResult[+T] = Future[ValidationResult[T]]
  type VideoFile             = MultipartFormData.FilePart[Files.TemporaryFile]
}
