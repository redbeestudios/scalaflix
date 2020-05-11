package services

import java.io.InputStream
import converters._
import akka.stream.scaladsl.{Source, StreamConverters}
import akka.util.ByteString
import cats.data.EitherT
import cats.implicits._
import domain.Genre
import domain.requests.FilmDTO
import globals.{ApplicationResult, MapMarkerContext}
import javax.inject.{Inject, Singleton}
import play.api.Logging
import play.api.libs.Files
import play.api.mvc.MultipartFormData
import services.MinioService._
import services.XluggerService._
import services.resources.FilmResourceHandler
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FilmService @Inject()(
    filmRepository: FilmResourceHandler,
    minioService: MinioService,
    xluggerService: XluggerService
  )(implicit ec: ExecutionContext)
    extends Logging {

  def getBy(genres: List[Genre])(implicit mapMarkerContext: MapMarkerContext): ApplicationResult[Seq[FilmDTO]] =
    filmRepository.listAvailable(genres)

  def save(film: FilmDTO)(implicit mapMarkerContext: MapMarkerContext): ApplicationResult[FilmDTO] =
    filmRepository.save(film).toApplicationResult()

  def upload(
      id: Int,
      film: MultipartFormData.FilePart[Files.TemporaryFile]
    )(implicit mapMarkerContext: MapMarkerContext
    ): ApplicationResult[FilmDTO] = {
    val fileSize = film.fileSize
    val filepath = film.ref.path.toString
    val duration = xluggerService.getVideoDuration(filepath)

    val result = for {
      _         <- EitherT(filmRepository.makeAvailable(id, duration))
      _         <- EitherT(minioService.uploadFilePath(FILMS_BUCKET, id.toString, filepath, fileSize))
      thumbnail <- EitherT(xluggerService.generateThumbnail(filepath = filepath, 2))
      _ <- EitherT(
        minioService.uploadInputStream(THUMBNAILS_BUCKET, s"${id.toString}.$IMAGE_FORMAT", thumbnail)
      )
      film <- EitherT(filmRepository.get(id))
    } yield film
    result.value
  }

  def downloadThumbnail(
      id: Int
    )(implicit mapMarkerContext: MapMarkerContext
    ): ApplicationResult[Source[ByteString, _]] =
    minioService
      .downloadFile(THUMBNAILS_BUCKET, s"${id.toString}.$IMAGE_FORMAT")
      .map(toSource)
      .toApplicationResult()

  def stream(id: Int)(implicit mapMarkerContext: MapMarkerContext): ApplicationResult[Source[ByteString, _]] =
    minioService.downloadFile(FILMS_BUCKET, id.toString).map(toSource).toApplicationResult()

  private def toSource(inputStream: InputStream): Source[ByteString, _] =
    StreamConverters
      .fromInputStream(() => inputStream)

}
