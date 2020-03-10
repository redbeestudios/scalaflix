package error

import javax.inject.Singleton
import play.api.Logging
import play.api.http.HttpErrorHandler
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc._

import scala.concurrent._

/**
  * Manage unhandled errors
  */
@Singleton
class ErrorHandler extends HttpErrorHandler with Logging {

  private val errorKey = "error"

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] =
    Future.successful {
      val msg =
        s"A client error occurred: $message (status $statusCode on path ${request.path})"
      logger.warn(msg)
      Status(statusCode)(Json.obj(errorKey -> msg))
    }

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] =
    Future.successful {
      val msg = s"A server error occurred: ${exception.getMessage}"
      logger.error(msg, exception)
      InternalServerError(Json.obj(errorKey -> msg))
    }

}
