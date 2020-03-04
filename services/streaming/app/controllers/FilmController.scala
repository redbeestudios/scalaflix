package controllers

import controllers.circe.CirceImplicits
import domain.Film
import domain.requests.FilmRequest
import io.circe.syntax._
import javax.inject._
import play.api.Logging
import play.api.libs.Files
import play.api.libs.circe.Circe
import play.api.mvc.{Action, _}
import services.FilmService
import io.circe.generic.auto._

import scala.concurrent._

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
    * Get all films
    */
  def getAll: Action[AnyContent] = Action.async { _ =>
    filmService.getBy(Nil) map { films =>
      Ok(films.asJson)
    }
  }

  /**
    * Create Film
    */
  def createFilm: Action[FilmRequest] = Action.async(circe.json[FilmRequest]) { implicit request =>
    val film: Film = request.body.toDomain
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
