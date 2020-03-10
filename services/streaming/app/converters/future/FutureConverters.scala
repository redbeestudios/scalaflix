package converters.future

import error.{ApplicationError, ExecutionError}
import globals.{ApplicationResult, MapMarkerContext, ValidationResult}
import play.api.Logging

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success}

trait FutureConverters extends Logging {

  implicit class FutureConverterOps[T](f: Future[T]) {

    private val defaultDescription = "Execution error."

    def toApplicationResult(applicationError: ApplicationError = ExecutionError(defaultDescription),
                            errorDescription: String = defaultDescription)(implicit ec: ExecutionContext,
                                                      mapMarkerContext: MapMarkerContext): ApplicationResult[T] = {
      val p = Promise[ValidationResult[T]]()
      f.onComplete {
        case Success(value) => p success Right(value)
        case Failure(exception) =>
          logger.error(errorDescription, exception)
          p success Left(applicationError)
      }
      p.future
    }
  }
}
