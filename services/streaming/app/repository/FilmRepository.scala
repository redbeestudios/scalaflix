package repository

import domain.Film
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Future

trait FilmRepository {
  def get(id: Long): Future[Film]

  def list(genres: List[String]): Future[List[Film]]
}

class FilmRepositoryImpl @Inject()
  (override val dbConfigProvider: DatabaseConfigProvider) extends FilmRepository with BaseRepository[Film] {

  import profile.api._

  private val filmTableQuery = TableQuery[FilmTable]

  def get(id: Long): Future[Film] = ???

  def list(genres: List[String]): Future[List[Film]] = ???


  private case class FilmTable(tag: Tag) extends Table[FilmTable](tag, "film") {
    override def * = ???
  }
}
