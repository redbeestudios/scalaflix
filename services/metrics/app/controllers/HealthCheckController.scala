package controllers

import javax.inject.Singleton
import play.api.http.MimeTypes
import play.api.mvc.{Action, AnyContent, InjectedController}

@Singleton
class HealthCheckController extends InjectedController {

  /**
    * Health check method to inform application's status
    * @return a status 200 with the build info
    */
  def check: Action[AnyContent] = Action {
    val objectName = "controllers.BuildInfo$"
    val cons       = Class.forName(objectName).getDeclaredConstructors

    cons(0).setAccessible(true)
    val buildInfo = cons(0).newInstance().asInstanceOf[ToJson].toJson

    Ok(buildInfo).as(MimeTypes.JSON)
  }

}

trait ToJson {

  def toJson: String

}
