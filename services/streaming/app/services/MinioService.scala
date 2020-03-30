package services

import java.io.InputStream

import configurations.EXTERNAL_DISPATCHER
import io.minio.MinioClient
import javax.inject.{Inject, Named, Singleton}
import play.api.Logging
import services.MinioService._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MinioService @Inject()(minioClient: MinioClient)(implicit @Named(EXTERNAL_DISPATCHER) ec: ExecutionContext)
    extends Logging {

  // Create films bucket if it does not exist
  if (!minioClient.bucketExists(FILMS_BUCKET)) {
    logger.info(s"Creating $FILMS_BUCKET bucket")
    minioClient.makeBucket(FILMS_BUCKET)
  }
  // Create thumbnails bucket if it does not exist
  if (!minioClient.bucketExists(THUMBNAILS_BUCKET)) {
    logger.info(s"Creating $THUMBNAILS_BUCKET bucket")
    minioClient.makeBucket(THUMBNAILS_BUCKET)
  }

  def uploadFilePath(bucket: String, filename: String, filepath: String, fileSize: Long): Future[Unit] =
    Future {
      minioClient.putObject(bucket, filename, filepath, fileSize, null, null, null)
    } recover {
      case e =>
        logger.error("Error uploading file to minio from path", e)
        throw e
    }

  def uploadInputStream(bucket: String, filename: String, fileInputStream: InputStream): Future[Unit] =
    Future {
      minioClient.putObject(bucket, filename, fileInputStream, null, null, null, null)
    } recover {
      case e =>
        logger.error("Error uploading file to minio", e)
        throw e
    }

  def downloadFile(bucket: String, filename: String): Future[InputStream] =
    Future {
      minioClient.getObject(bucket, filename)
    } recover {
      case e =>
        logger.error("Error downloading file from minio", e)
        throw e
    }

}

object MinioService {
  final val FILMS_BUCKET      = "films"
  final val THUMBNAILS_BUCKET = "thumbnails"
}
