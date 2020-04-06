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

  def addView(id: Long): Future[Unit] =
    wsClient
      .url(s"${metricsConfiguration.host}:${metricsConfiguration.port}${addViewPath(id)}")
      .put("")
      .map { response =>
        response.status match {
          case 200 => logger.info(s"Successfully added view on metrics for film $id.")
          case _   => logger.error(s"Error adding metrics view for film $id, response body: ${response.body}.")
        }
      }
      .recover {
        case t => logger.error(s"Unknown error adding metrics view for film $id.", t)
      }
}
