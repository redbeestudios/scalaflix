package controllers

import _root_.validation.FilmValidations
import cats.data.EitherT
import cats.implicits._
import controllers.circe.CirceImplicits
import converters._
import domain.Film
import domain.requests.FilmRequest
import globals.MapMarkerContext
import io.circe.syntax._
import javax.inject._
import play.api.Logging
import play.api.libs.Files
import play.api.libs.circe.Circe
import play.api.mvc.{Action, _}
import services.FilmService

import scala.concurrent._

/**
  * This controller handles the Films CRUD operations
  */
@Singleton
class FilmController @Inject()(cc: ControllerComponents, filmService: FilmService)(implicit ec: ExecutionContext)
    extends AbstractController(cc)
    with FilmValidations
    with CirceImplicits
    with Circe
    with Logging {

  val SIZE_100MB: Long = 1024 * 1024 * 100

  /**
    * Get all films
    */
  def getAll: Action[AnyContent] = Action.async { _ =>
    filmService.getBy(Nil)(MapMarkerContext()).toOkResult
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
      val uploadExecution = for {
        videoFile <- EitherT(validateVideoFileHeader(request.body.file("film")).toApplicationResult)
        _         <- EitherT(filmService.uploadFilm(id, videoFile)(MapMarkerContext()))
      } yield ()
      uploadExecution.value.toCreatedResult
    }

  /**
    * Get Film
    */
  def downloadFilm(id: Int): Action[AnyContent] = Action.async { _ =>
    logger.info(s"Downloading film with id: $id")
    filmService.downloadFilm(id)(MapMarkerContext()).toOkResult
  }

}
