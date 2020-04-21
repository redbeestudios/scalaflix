package controllers

import cats.data.EitherT
import cats.implicits._
import controllers.circe.CirceImplicits
import domain.Genre
import domain.requests.FilmDTO
import io.circe.syntax._
import javax.inject._
import json.Decodable
import play.api.Logging
import play.api.libs.Files
import play.api.mvc.{Action, _}
import services.{FilmService, MetricsService}
import services.XluggerService.IMAGE_FORMAT
import validation.FilmValidations
import converters._
import globals.MapMarkerContext

import scala.concurrent._

/**
  * This controller handles the Films CRUD operations
  */
@Singleton
class FilmController @Inject()(
    filmService: FilmService,
    metricsService: MetricsService,
    cc: ControllerComponents
  )(implicit ec: ExecutionContext)
    extends AbstractController(cc)
    with FilmValidations
    with CirceImplicits
    with Decodable
    with Logging {

  val SIZE_100MB: Long = 1024 * 1024 * 100

  /**
    * Get all films
    */
  def getAll(genres: List[String]): Action[AnyContent] = Action.async { _ =>
    implicit val mmc: MapMarkerContext = MapMarkerContext()
    filmService.getBy(genres.map(Genre)).toOkResult
  }

  /**
    * Create Film
    */
  def createFilm: Action[FilmDTO] = Action.async(circe.json[FilmDTO]) { implicit request =>
    implicit val mmc: MapMarkerContext = MapMarkerContext()
    val film: FilmDTO                  = request.body.copy(id = None)
    logger.info(s"Creating Film: ${request.body.asJson.noSpaces}")
    filmService.save(film).toCreatedResult
  }

  /**
    * Upload Film
    */
  def uploadFilm(id: Int): Action[MultipartFormData[Files.TemporaryFile]] =
    Action.async(parse.multipartFormData(maxLength = SIZE_100MB)) { implicit request =>
      val uploadExecution = for {
        videoFile <- EitherT(validateVideoFileHeader(request.body.file("film")).toApplicationResult)
        film      <- EitherT(filmService.upload(id, videoFile)(MapMarkerContext()))
      } yield film
      uploadExecution.value.toCreatedResult
    }

  /**
    * Get Film
    */
  def stream(id: Int): Action[AnyContent] = Action.async { _ =>
    implicit val mmc: MapMarkerContext = MapMarkerContext()
    logger.info(s"Downloading film with id: $id")
    metricsService.addView(id)
    filmService.stream(id).toMediaResult
  }

  /**
    * Get Film
    */
  def downloadThumbnail(id: Int): Action[AnyContent] = Action.async { _ =>
    implicit val mmc: MapMarkerContext = MapMarkerContext()
    logger.info(s"Downloading film with id: $id")
    filmService.downloadThumbnail(id).toMediaResult map (_.as(s"image/$IMAGE_FORMAT"))
  }

}
