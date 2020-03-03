package repositories

import com.google.inject.Singleton
import domain.Film
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Future

trait FilmRepository extends BaseRepository[Film] {
  def get(id: Long): Future[Film]

  def list(genres: List[String]): Future[List[Film]]
}

@Singleton
class FilmRepositoryImpl @Inject()(override val dbConfigProvider: DatabaseConfigProvider) extends FilmRepository {

  import profile.api._

  def get(id: Long): Future[Film] = ???

  def list(genres: List[String]): Future[List[Film]] = ???

}
