package services

import java.io.InputStream

import akka.stream.scaladsl.{Source, StreamConverters}
import akka.util.ByteString
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

  def upload(id: Int, film: MultipartFormData.FilePart[Files.TemporaryFile]): Future[Film] = {
    val fileSize = film.fileSize
    val filepath = film.ref.path.toString
    val duration = xluggerService.getVideoDuration(filepath)

    for {
      _         <- filmRepository.makeAvailable(id, duration)
      _         <- minioService.uploadFilePath(FILMS_BUCKET, id.toString, filepath, fileSize)
      thumbnail <- xluggerService.generateThumbnail(filepath = filepath, 2)
      _         <- minioService.uploadInputStream(THUMBNAILS_BUCKET, s"${id.toString}.$IMAGE_FORMAT", thumbnail)
      film      <- filmRepository.get(id)
    } yield film
  }

  def downloadThumbnail(id: Int): Future[Source[ByteString, _]] =
    minioService.downloadFile(THUMBNAILS_BUCKET, s"${id.toString}.$IMAGE_FORMAT") map toSource

  def stream(id: Int): Future[Source[ByteString, _]] =
    minioService.downloadFile(FILMS_BUCKET, id.toString) map toSource

  private def toSource = { inputStream: InputStream =>
    StreamConverters.fromInputStream(() => inputStream)
  }

}
