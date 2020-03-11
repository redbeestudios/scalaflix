package services

import java.io.InputStream

import globals.{ApplicationResult, MapMarkerContext}
import io.minio.MinioClient
import javax.inject.{Inject, Singleton}
import play.api.Logging

import scala.concurrent.{ExecutionContext, Future}
import converters._

@Singleton
class MinioService @Inject()(minioClient: MinioClient)(implicit ec: ExecutionContext) extends Logging {

  final val FILMS_BUCKET = "films"

  // Create films bucket if it does not exist
  if (!minioClient.bucketExists(FILMS_BUCKET)) {
    logger.info("Creating films bucket")
    minioClient.makeBucket(FILMS_BUCKET)
  }

  def uploadFilm(
      id: Int,
      filepath: String,
      fileSize: Long
    )(implicit mapMarkerContext: MapMarkerContext
    ): ApplicationResult[Unit] =
    Future {
      minioClient.putObject(
        FILMS_BUCKET,
        getFilename(id),
        filepath,
        fileSize,
        null, // scalastyle:ignore
        null, // scalastyle:ignore
        null // scalastyle:ignore
      )
    }.toApplicationResult()

  def downloadFilm(id: Int)(implicit mapMarkerContext: MapMarkerContext): ApplicationResult[InputStream] =
    Future {
      minioClient.getObject(FILMS_BUCKET, getFilename(id))
    }.toApplicationResult()

  private def getFilename(id: Int): String = id.toString
}
