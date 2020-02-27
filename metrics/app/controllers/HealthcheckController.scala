package controllers

import javax.inject._
import play.api.mvc._
import config.BuildInfo

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HealthcheckController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Healhcheck
   */
  def healthcheck = Action { implicit request: Request[AnyContent] =>
    Ok(BuildInfo.toJson)
  }

}
