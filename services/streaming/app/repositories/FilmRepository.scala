package repositories

import com.google.inject.Singleton
import domain.{Film, Genre}
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import repositories.tables.{FilmTable, FilmXGenreTable, GenreTable}

import scala.concurrent.{ExecutionContext, Future}

trait FilmRepository extends BaseRepository[Film] {
  def list(genres: List[Genre]): Future[Seq[Film]]
}

@Singleton
class FilmRepositoryImpl @Inject()(override val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
    extends FilmRepository {

  import profile.api._

  def list(genres: List[Genre]): Future[Seq[Film]] =
    db.run(createListQuery(genres).result) map { filmGenreTuple =>
      filmGenreTuple
        .groupBy(_._1)
        .map {
          case (film, filmGenreTuples) =>
            val genres = filmGenreTuples.flatMap(_._2).distinct.toList
            film.copy(genres = genres)
        }
        .toList
    }

  private def createListQuery(genres: List[Genre]) = {
    val filmXGenreTableQuery = FilmXGenreTable.table.filter(_.genre inSet genres.map(_.value))
    for {
      ((film, _), genre) <- FilmTable.table
        .joinLeft(filmXGenreTableQuery)
        .on(_.id === _.filmId)
        .joinLeft(GenreTable.table)
        .on(_._2.map(_.genre) === _.value)
    } yield (film, genre)
  }

}
