package services

import java.io.InputStream

import akka.stream.scaladsl.{Source, StreamConverters}
import akka.util.ByteString
import com.xuggle.xuggler.IContainer
import domain.{Film, Genre}
import javax.inject.{Inject, Singleton}
import play.api.Logging
import play.api.libs.Files
import play.api.mvc.MultipartFormData
import repositories.FilmRepository
import services.MinioService._
import services.XluggerService._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FilmService @Inject()(
    filmRepository: FilmRepository,
    minioService: MinioService,
    xluggerService: XluggerService
  )(implicit ec: ExecutionContext)
    extends Logging {

  def getBy(genres: List[Genre]): Future[Seq[Film]] =
    filmRepository.listAvailable(genres)

  def save(film: Film): Future[Film] =
    filmRepository.save(film)

  def upload(id: Int, film: MultipartFormData.FilePart[Files.TemporaryFile]): Future[Unit] = {
    val fileSize = film.fileSize
    val filepath = film.ref.path.toString

    for {
      _ <- minioService.uploadFilePath(FILMS_BUCKET, id.toString, filepath, fileSize)
      duration = getVideoDuration(filepath)
      thumbnail <- xluggerService.generateThumbnail(filepath = filepath, 2)
      _         <- minioService.uploadInputStream(THUMBNAILS_BUCKET, s"${id.toString}.$IMAGE_FORMAT", thumbnail)
      _         <- filmRepository.makeAvailable(id, duration)
    } yield ()
  }

  def downloadThumbnail(id: Int): Future[Source[ByteString, _]] =
    minioService.downloadFile(THUMBNAILS_BUCKET, s"${id.toString}.$IMAGE_FORMAT") map toSource

  def stream(id: Int): Future[Source[ByteString, _]] =
    minioService.downloadFile(FILMS_BUCKET, id.toString) map toSource

  private def toSource = { inputStream: InputStream =>
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
