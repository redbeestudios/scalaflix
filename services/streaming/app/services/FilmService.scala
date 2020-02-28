package services

import akka.stream.scaladsl.{ Source, StreamConverters }
import akka.util.ByteString
import com.xuggle.xuggler.IContainer
import javax.inject.{ Inject, Singleton }
import play.api.Logging
import play.api.libs.Files
import play.api.mvc.MultipartFormData

import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class FilmService @Inject()(minioService: MinioService)(implicit ec: ExecutionContext) extends Logging {

  def uploadFilm(id: Int, film: MultipartFormData.FilePart[Files.TemporaryFile]): Future[Unit] = {
    val fileSize = film.fileSize
    val filepath = film.ref.path.toString

    for {
      _ <- minioService.uploadFilm(id, filepath, fileSize)
      // TODO create and save thumbnail
      duration = getVideoDuration(filepath)
      // TODO save to DB
    } yield ()
  }

  def downloadFilm(id: Int): Future[Source[ByteString, _]] =
    minioService.downloadFilm(id) map { inputStream =>
      StreamConverters.fromInputStream(() => inputStream)
    }

  private def getVideoDuration(filepath: String): Long = {
    // first we create a Xuggler container object// first we create a Xuggler container object
    val container = IContainer.make

    // we attempt to open up the container
    val result = container.open(filepath, IContainer.Type.READ, null)

    // check if the operation was successful
    if (result < 0) throw new RuntimeException("Failed to open media file")

    // query for the total duration
    container.getDuration
  }

}
