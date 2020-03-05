package controllers

import javax.inject.Singleton
import play.api.http.MimeTypes
import play.api.mvc._

/**
  * This controller creates an [[play.api.mvc.Action Action]] to handle HTTP requests
  * for health checks.
  */
@Singleton
class HealthCheckController extends InjectedController {

  /**
    * Health check method to inform application's status.
    *
    * @return A status 200 with a fixed response.
    */
  def healthcheck: Action[AnyContent] = Action {
    Ok("""{"status":"ok"}""").as(MimeTypes.JSON)
  }

}
