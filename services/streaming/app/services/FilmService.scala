package services

import java.io.InputStream

import akka.stream.scaladsl.{Source, StreamConverters}
import akka.util.ByteString
import cats.data.EitherT
import cats.implicits._
import domain.{Film, Genre}
import globals.{ApplicationResult, MapMarkerContext}
import javax.inject.{Inject, Singleton}
import play.api.Logging
import play.api.libs.Files
import play.api.mvc.MultipartFormData
import repositories.FilmRepository
import services.MinioService._
import services.XluggerService._

import scala.concurrent.ExecutionContext

@Singleton
class FilmService @Inject()(
    filmRepository: FilmRepository,
    minioService: MinioService,
    xluggerService: XluggerService
  )(implicit ec: ExecutionContext)
    extends Logging {

  def getBy(genres: List[Genre])(implicit mapMarkerContext: MapMarkerContext): ApplicationResult[Seq[Film]] =
    filmRepository.listAvailable(genres)

  def save(film: Film)(implicit mapMarkerContext: MapMarkerContext): ApplicationResult[Film] =
    filmRepository.save(film)

  def upload(id: Int, film: MultipartFormData.FilePart[Files.TemporaryFile])
	(implicit mapMarkerContext: MapMarkerContext): ApplicationResult[Unit] = {
    val fileSize = film.fileSize
    val filepath = film.ref.path.toString
    val duration = xluggerService.getVideoDuration(filepath)

    val uploadExecution = for {
      _         <- EitherT(filmRepository.makeAvailable(id, duration))
      _         <- EitherT(minioService.uploadFilePath(FILMS_BUCKET, id.toString, filepath, fileSize))
      thumbnail <- EitherT(xluggerService.generateThumbnail(filepath = filepath, 2))
      _         <- EitherT(minioService.uploadInputStream(
        THUMBNAILS_BUCKET,
        s"${id.toString}.$IMAGE_FORMAT",
        thumbnail
      ))
      _         <- EitherT(filmRepository.get(id))
    } yield ()
    uploadExecution.value
  }

  def downloadThumbnail(id: Int)(
    implicit mapMarkerContext: MapMarkerContext): ApplicationResult[Source[ByteString, _]] =
    minioService.downloadFile(
      THUMBNAILS_BUCKET,
      s"${id.toString}.$IMAGE_FORMAT") map (result => result map toSource)

  def stream(id: Int)(implicit mapMarkerContext: MapMarkerContext): ApplicationResult[Source[ByteString, _]] =
    minioService.downloadFile(FILMS_BUCKET, id.toString) map (result => result map toSource)

  private def toSource = { inputStream: InputStream =>
    StreamConverters.fromInputStream(() => inputStream)
  }
}
