package converters.result

import globals.ApplicationResult
import io.circe.{Decoder, Encoder}
import play.api.mvc.Result

trait ApplicationResultConverters {
  implicit class ApplicationResultOps[T](applicationResult: ApplicationResult[T]) {
    def toCreatedResult(implicit decoder: Decoder[T]): Result = ???

    def tuSucceededResult(implicit encoder: Encoder[T]): Result = ???
  }
}
