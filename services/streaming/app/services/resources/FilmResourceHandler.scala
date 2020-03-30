package services.resources

import domain.requests.FilmDTO
import domain.{Film, Genre}
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.repositories.FilmRepository
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FilmResourceHandler @Inject()(
    val dbConfigProvider: DatabaseConfigProvider,
    repository: FilmRepository
  )(implicit ec: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {

  def get(id: Long): Future[FilmDTO] = db.run(this.repository.get(id)) map toDTO

  def listAvailable(genres: List[Genre]): Future[List[FilmDTO]] = db.run {
    this.repository.listAvailable(genres) map (_ map toDTO)
  }

  def save(film: FilmDTO): Future[FilmDTO] = db.run {
    val dbFilm = Film(name = film.name, description = film.description)

    this.repository.save(dbFilm, film.genres) map (saved => toDTO(saved, film.genres))
  }

  def makeAvailable(id: Long, duration: Long): Future[Int] = db.run(this.repository.makeAvailable(id, duration))

  private def toDTO(tuple: (Film, List[Genre])): FilmDTO = tuple match {
    case (film, genres) => FilmDTO(film.id, film.name, film.description, genres)
  }

}
