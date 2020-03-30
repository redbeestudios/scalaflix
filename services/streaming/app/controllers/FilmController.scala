package controllers

import controllers.circe.CirceImplicits
import domain.Genre
import domain.requests.FilmDTO
import io.circe.generic.auto._
import io.circe.syntax._
import javax.inject._
import play.api.Logging
import play.api.libs.Files
import play.api.libs.circe.Circe
import play.api.mvc.{Action, _}
import services.FilmService
import services.XluggerService.IMAGE_FORMAT

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
  def getAll(genres: List[String]): Action[AnyContent] = Action.async { _ =>
    filmService.getBy(genres.map(Genre)) map { films =>
      Ok(films.asJson)
    }
  }

  /**
    * Create Film
    */
  def createFilm: Action[FilmDTO] = Action.async(circe.json[FilmDTO]) { implicit request =>
    val film = request.body

    logger.info(s"Creating Film: ${film.asJson.noSpaces}")

    filmService.save(film) map { insertedFilm =>
      Created(insertedFilm.asJson)
    }
  }

  /**
    * Upload Film
    */
  def uploadFilm(id: Int): Action[MultipartFormData[Files.TemporaryFile]] =
    Action.async(parse.multipartFormData(maxLength = SIZE_100MB)) { implicit request =>
      request.body
        .file("film")
        .map(file => filmService.upload(id, file).map(film => Ok(film.asJson)))
        .getOrElse(Future.successful(BadRequest("Missing \"film\" key")))
    }

  /**
    * Get Film
    */
  def stream(id: Int): Action[AnyContent] = Action.async { _ =>
    logger.info(s"Downloading film with id: $id")
    filmService.stream(id).map(Ok.chunked(_))
  }

  /**
    * Get Film
    */
  def downloadThumbnail(id: Int): Action[AnyContent] = Action.async { _ =>
    logger.info(s"Downloading film with id: $id")
    filmService.downloadThumbnail(id).map(Ok.chunked(_).as(s"image/$IMAGE_FORMAT"))
  }

}
