package services.resources

import domain.requests.FilmDTO
import domain.{Film, Genre}
import globals.{ApplicationResult, MapMarkerContext}
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.repositories.FilmRepository
import slick.jdbc.JdbcProfile
import converters._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FilmResourceHandler @Inject()(
    val dbConfigProvider: DatabaseConfigProvider,
    repository: FilmRepository
  )(implicit ec: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {

  def get(id: Long)(implicit mapMarkerContext: MapMarkerContext): ApplicationResult[FilmDTO] =
    (db.run(this.repository.get(id)) map toDTO).toApplicationResult()

  def listAvailable(
      genres: List[Genre]
    )(implicit mapMarkerContext: MapMarkerContext
    ): ApplicationResult[List[FilmDTO]] =
    db.run {
        this.repository.listAvailable(genres) map (_ map toDTO)
      }
      .toApplicationResult()

  def save(film: FilmDTO): Future[FilmDTO] = db.run {
    val dbFilm = Film(name = film.name, description = film.description)

    this.repository.save(dbFilm, film.genres) map (saved => toDTO(saved, film.genres))
  }

  def makeAvailable(id: Long, duration: Long)(implicit mapMarkerContext: MapMarkerContext): ApplicationResult[Int] =
    db.run(this.repository.makeAvailable(id, duration)).toApplicationResult()

  private def toDTO(tuple: (Film, List[Genre])): FilmDTO = tuple match {
    case (film, genres) => FilmDTO(film.id, film.name, film.description, genres)
  }

}
