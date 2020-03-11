package services

import akka.stream.scaladsl.{Source, StreamConverters}
import akka.util.ByteString
import com.xuggle.xuggler.IContainer
import domain.{Film, Genre}
import error.ExecutionError
import globals.{ApplicationResult, MapMarkerContext}
import javax.inject.{Inject, Singleton}
import play.api.Logging
import play.api.libs.Files
import play.api.mvc.MultipartFormData
import repositories.FilmRepository

import converters._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FilmService @Inject()(minioService: MinioService, filmRepository: FilmRepository)(implicit ec: ExecutionContext)
    extends Logging {

  def getBy(genres: List[Genre])(implicit mapMarkerContext: MapMarkerContext): ApplicationResult[Seq[Film]] =
    filmRepository.list(genres).toApplicationResult()

  def uploadFilm(
      id: Int,
      film: MultipartFormData.FilePart[Files.TemporaryFile]
    )(implicit mapMarkerContext: MapMarkerContext
    ): ApplicationResult[Unit] = {
    val fileSize = film.fileSize
    val filepath = film.ref.path.toString

    for {
      result <- minioService.uploadFilm(id, filepath, fileSize)
      // TODO create and save thumbnail
      duration = getVideoDuration(filepath)
      // TODO save to DB
    } yield result
  }

  def downloadFilm(id: Int)(implicit mapMarkerContext: MapMarkerContext): ApplicationResult[Source[ByteString, _]] =
    minioService.downloadFilm(id) map { result =>
      result map { inputStream =>
        StreamConverters.fromInputStream(() => inputStream)
      }
    }

  private def getVideoDuration(filepath: String): ApplicationResult[Long] = {
    // first we create a Xuggler container object// first we create a Xuggler container object
    val container = IContainer.make

    // we attempt to open up the container
    val result = container.open(
      filepath,
      IContainer.Type.READ,
      null // scalastyle:ignore
    )

    // check if the operation was successful
    Future.successful {
      if (result < 0) Left(ExecutionError("Failed to open media file")) else Right(container.getDuration)
    }
  }

}
