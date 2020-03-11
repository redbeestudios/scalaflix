package converters.result

import controllers.circe.CirceImplicits
import error.{ApplicationError, ExecutionError, NotFoundError, ValidationError}
import globals.ApplicationResult
import io.circe.{Decoder, Encoder}
import play.api.mvc.{Result, Results}

import scala.concurrent.{ExecutionContext, Future}
import io.circe.syntax._

trait ApplicationResultConverters extends Results with CirceImplicits {

  private def handleApplicationError(error: ApplicationError): Result =
    error match {
      case executionError: ExecutionError   => InternalServerError(executionError.asJson)
      case validationError: ValidationError => BadRequest(validationError.asJson)
      case notFoundError: NotFoundError     => NotFound(notFoundError.asJson)
    }

  implicit class ApplicationResultOps[T](applicationResult: ApplicationResult[T]) {

    def toCreatedResult(implicit ec: ExecutionContext, decoder: Decoder[T]): Future[Result] =
      applicationResult map { validationResult =>
        {
          validationResult.fold(handleApplicationError, result => Created(result.asJson))
        }
      }

    def toOkResult(implicit ec: ExecutionContext, encoder: Encoder[T]): Future[Result] =
      applicationResult map { validationResult =>
        validationResult.fold(handleApplicationError, result => Ok(result.asJson))
      }
  }
}
