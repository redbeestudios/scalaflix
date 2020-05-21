package converters.result

import akka.stream.scaladsl.Source
import akka.util.ByteString
import controllers.circe.CirceImplicits
import error.{ApplicationError, ExecutionError, NotFoundError, ValidationError}
import globals.ApplicationResult
import io.circe.syntax._
import io.circe.{Encoder, Json}
import play.api.http.MimeTypes
import play.api.mvc.{Result, Results}

import scala.concurrent.{ExecutionContext, Future}

trait ApplicationResultConverters extends Results with CirceImplicits {

  implicit private def circeWritable: play.api.http.Writeable[Json] =
    new play.api.http.Writeable[Json](json => ByteString(json.toString()), Some(MimeTypes.JSON))

  private def handleApplicationError(error: ApplicationError): Result = ???

  implicit class ApplicationResultOps[T](applicationResult: ApplicationResult[T]) {

    def toCreatedResult(implicit ec: ExecutionContext, encoder: Encoder[T]): Future[Result] =
      applicationResult map (_.fold(handleApplicationError, result => Created(result.asJson)))

    def toOkResult(implicit ec: ExecutionContext, encoder: Encoder[T]): Future[Result] =
      applicationResult map (_.fold(handleApplicationError, result => Ok(result.asJson)))

    def toMediaResult(implicit ec: ExecutionContext): Future[Result] =
      applicationResult map (_.fold(handleApplicationError, {
        case stream: Source[ByteString, _] => Ok.chunked(stream)
        case _                             => InternalServerError
      }))
  }
}
