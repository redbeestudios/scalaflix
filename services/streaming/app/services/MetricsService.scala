package services

import configurations.MetricsConfiguration
import javax.inject.{Inject, Singleton}
import play.api.Logging
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MetricsService @Inject()(
    wsClient: WSClient,
    metricsConfiguration: MetricsConfiguration
  )(implicit ec: ExecutionContext)
    extends Logging {

  def addViewPath(filmId: Long): String = s"/metrics/$filmId/views"

  def addView(id: Long): Future[Unit] = ???
}
