package controllers

import java.time.LocalDateTime

import controllers.circe.CirceImplicits
import io.circe.Json
import io.circe.syntax._
import javax.inject.{Inject, Singleton}
import models.{FilmId, FilmMetrics}
import play.api.libs.circe.Circe
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import services.resources.ViewResourceHandler

import scala.concurrent.ExecutionContext

/**
  * Metrics handler controller.
  *
  * @param controllerComponents Components required by the controller to perform its job (it's a Play requirement).
  * @param viewHandler          An instance of [[services.resources.ViewResourceHandler]].
  * @param ec                   An implicit [[scala.concurrent.ExecutionContext]].
  */
@Singleton
class MetricsController @Inject()(
    val controllerComponents: ControllerComponents,
    viewHandler: ViewResourceHandler
  )(implicit ec: ExecutionContext)
    extends BaseController
    with CirceImplicits
    with Circe {

  /**
    * List metrics for all films.
    *
    * @param from An optional date to filter results.
    * @return An asynchronous [[play.api.mvc.BaseController#Action]].
    */
  def listMetrics(from: Option[LocalDateTime]): Action[AnyContent] = Action.async {
    this.viewHandler.listFilmsViews(from) map { views =>
      val metrics = views.map {
        case (filmId, views) => FilmMetrics(id = filmId, views = views)
      }

      Ok(Json.obj("films" -> metrics.asJson))
    }
  }

  /**
    * Adds a view to the given film.
    *
    * @param filmId The film identifier.
    * @return An asynchronous [[play.api.mvc.BaseController#Action]].
    */
  def addView(filmId: FilmId): Action[AnyContent] = Action.async {
    this.viewHandler.addView(filmId) map { _ =>
      NoContent
    }
  }

}
