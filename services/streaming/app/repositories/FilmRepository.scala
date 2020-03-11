package repositories

import com.google.inject.Singleton
import domain.{Film, Genre}
import globals.{ApplicationResult, MapMarkerContext}
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import repositories.tables.{FilmTable, FilmXGenreTable}
import converters._
import scala.concurrent.{ExecutionContext, Future}

trait FilmRepository extends BaseRepository[Film] {
  def listAvailable(genres: List[Genre])(implicit mapMarkerContext: MapMarkerContext): ApplicationResult[Seq[Film]]
  def get(id: Int)(implicit mapMarkerContext: MapMarkerContext): ApplicationResult[Film]
  def save(film: Film)(implicit mapMarkerContext: MapMarkerContext): ApplicationResult[Film]
  def makeAvailable(id: Int, duration: Long)(implicit mapMarkerContext: MapMarkerContext): ApplicationResult[Int]
}

@Singleton
class FilmRepositoryImpl @Inject()(override val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
    extends FilmRepository {

  import profile.api._

  def get(id: Int)(implicit mapMarkerContext: MapMarkerContext): ApplicationResult[Film] = {
    val filmsQuery = FilmTable.table.filter(_.id === id)
    db.run(withGenresQuery(filmsQuery).result) map (toFilms(_).head) toApplicationResult()
  }

  def listAvailable(genres: List[Genre])(
    implicit mapMarkerContext: MapMarkerContext): ApplicationResult[Seq[Film]] = {
    val filmsQuery = FilmTable.table.filter(_.available === true)
    db.run(withGenresQuery(filmsQuery).result)
      .map(toFilms(_)
      .filter(_.genres.exists(genres.contains(_)))) toApplicationResult()
  }

  private def withGenresQuery(filmsQuery: Query[FilmTable, FilmTable#TableElementType, Seq]) =
    for {
      (film, filmXGenre) <- filmsQuery
        .joinLeft(FilmXGenreTable.table)
        .on(_.id === _.filmId)
    } yield (film, filmXGenre.map(_.genre))

  private def toFilms(filmGenreTuple: Seq[(Film, Option[String])]): Seq[Film] =
    filmGenreTuple
      .groupBy(_._1)
      .map {
        case (film, filmGenreTuples) =>
          val genres = filmGenreTuples.flatMap(tuple => tuple._2.map(Genre)).distinct.toList
          film.copy(genres = genres)
      }
      .toList

  def save(film: Film)(implicit mapMarkerContext: MapMarkerContext): ApplicationResult[Film] =
    db.run(createSaveAction(film).transactionally) toApplicationResult()

  private def createSaveAction(film: Film) =
    for {
      filmId <- FilmTable.table += film
      _      <- FilmXGenreTable.table ++= film.genres.map(genre => (filmId, genre.value))
    } yield film.copy(id = Some(filmId))

  def makeAvailable(id: Int, duration: Long)(implicit mapMarkerContext: MapMarkerContext): ApplicationResult[Int] =
    db.run(
      FilmTable.table
        .filter(_.id === id)
        .map(film => (film.duration, film.available))
        .update((duration, true))
    ) toApplicationResult()

}
