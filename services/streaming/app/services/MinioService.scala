package services

import java.io.InputStream

import io.minio.MinioClient
import javax.inject.{ Inject, Singleton }
import play.api.Logging

import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class MinioService @Inject()(minioClient: MinioClient)(implicit ec: ExecutionContext) extends Logging {

  final val FILMS_BUCKET = "films"

  // Create films bucket if it does not exist
  if (!minioClient.bucketExists(FILMS_BUCKET)) {
    logger.info("Creating films bucket")
    minioClient.makeBucket(FILMS_BUCKET)
  }

  def uploadFilm(id: Int, filepath: String, fileSize: Long): Future[Unit] =
    Future {
      minioClient.putObject(FILMS_BUCKET, getFilename(id), filepath, fileSize, null, null, null)
    } recover {
      case e =>
        logger.error("Error: ", e)
        throw e
    }

  def downloadFilm(id: Int): Future[InputStream] =
    Future {
      minioClient.getObject(FILMS_BUCKET, getFilename(id))
    } recover {
      case e =>
        logger.error("Error: ", e)
        throw e
    }

  private def getFilename(id: Int): String = id.toString
}
