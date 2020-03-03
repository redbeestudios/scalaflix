package controllers

import controllers.circe.CirceImplicits
import io.circe.syntax._
import javax.inject._
import play.api.Logging
import play.api.libs.circe.Circe
import play.api.mvc.{Action, _}
import repositories.GenreRepository

import scala.concurrent._

/**
  * This controller handles the Films CRUD operations
  */
@Singleton
class GenreController @Inject()(
    cc: ControllerComponents,
    genreRepository: GenreRepository
  )(implicit ec: ExecutionContext)
    extends AbstractController(cc)
    with CirceImplicits
    with Circe
    with Logging {

  val SIZE_100MB: Long = 1024 * 1024 * 100

  /**
    * Get all Genres
    */
  def getAll: Action[AnyContent] = Action.async { _ =>
    genreRepository.list map { genres =>
      Ok(genres.asJson)
    }
  }

}
