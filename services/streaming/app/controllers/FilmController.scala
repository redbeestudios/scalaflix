package controllers

import controllers.circe.CirceImplicits
import domain.Film
import globals.MapMarkerContext
import io.circe.generic.auto._
import io.circe.syntax._
import javax.inject._
import play.api.Logging
import play.api.http.HttpEntity
import play.api.libs.Files
import play.api.libs.circe.Circe
import play.api.mvc.{ Action, _ }
import services.FilmService

import scala.concurrent.{ ExecutionContext, Future }

/**
 * This controller handles the Films CRUD operations
 */
@Singleton
class FilmController @Inject()(cc: ControllerComponents, filmService: FilmService)(implicit ec: ExecutionContext)
    extends AbstractController(cc)
    with CirceImplicits
    with Circe
    with Logging {

  val SIZE_100MB: Long = 1024 * 1024 * 100

  /**
   * Create Film
   */
  def createFilm(): Action[Film] = Action.async(circe.json[Film]) { implicit request =>
    val film = request.body
    logger.info(s"Creating Film: ${film.asJson.noSpaces}")
    // TODO save to DB
    Future(Ok(film.copy(id = Some(1)).asJson))
  }

  /**
   * Upload Film
   */
  def uploadFilm(id: Int): Action[MultipartFormData[Files.TemporaryFile]] =
    Action.async(parse.multipartFormData(maxLength = SIZE_100MB)) { implicit request =>
      request.body
        .file("film")
        .map(file => filmService.uploadFilm(id, file).map(_ => Ok("File saved successfully!")))
        .getOrElse(Future.successful(BadRequest("Missing \"film\" key")))
    }

  /**
   * Get Film
   */
  def downloadFilm(id: Int): Action[AnyContent] = Action.async { _ =>
    logger.info(s"Downloading film with id: $id")
    filmService.downloadFilm(id).map(Ok.chunked(_))
  }

}
