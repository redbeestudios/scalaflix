package services.repositories

import java.time.LocalDateTime

import com.google.inject.Singleton
import domain.{Film, Genre}
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FilmRepository @Inject()(
    val dbConfigProvider: DatabaseConfigProvider,
    val genreRepository: GenreRepository
  )(implicit ec: ExecutionContext)
    extends BaseRepository {

  import profile.api._

  val table = TableQuery[FilmTable]

  private val filmXGenreTable = TableQuery[FilmXGenre]

  def get(id: Long): DBIO[(Film, List[Genre])] = {
    val filmsQuery = this.table.filter(_.id === id)
    this.withGenresQuery(filmsQuery).result map (this.groupWithGenres(_).head)
  }

  def listAvailable(genres: List[Genre]): DBIO[List[(Film, List[Genre])]] =
    this
      .withGenresQuery(this.table)
      .filter {
        case (film, xGenre) => film.available === true && xGenre.inSet(genres)
      }
      .result
      .map(this.groupWithGenres)

  def save(film: Film, genres: List[Genre]): DBIO[Film] = {
    for {
      filmId <- this.table.returning(this.table.map(_.id)) += film
      _      <- this.filmXGenreTable ++= genres.map(genre => (filmId, genre))
    } yield film.copy(id = Some(filmId))
  }.transactionally

  def makeAvailable(id: Long, duration: Long): DBIO[Int] =
    this.table
      .filter(_.id === id)
      .map(film => (film.duration, film.available))
      .update((duration, true))

  private def withGenresQuery(filmsQuery: Query[FilmTable, FilmTable#TableElementType, Seq]) =
    for {
      (film, filmXGenre) <- filmsQuery
        .join(this.filmXGenreTable)
        .on(_.id === _.filmId)
    } yield (film, filmXGenre.genre)

  private def groupWithGenres(filmGenreTuple: Seq[(Film, Genre)]) =
    filmGenreTuple
      .groupBy(_._1)
      .map {
        case (film, filmGenreTuples) =>
          val genres = filmGenreTuples.map(tuple => tuple._2).distinct.toList
          film -> genres
      }
      .toList

  // $COVERAGE-OFF$
  // scalastyle:off
  class FilmTable(tag: Tag) extends Table[Film](tag, "films") {

    def id          = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name        = column[String]("name")
    def description = column[String]("description")
    def duration    = column[Long]("duration")
    def uploadDate  = column[LocalDateTime]("upload_date")
    def available   = column[Boolean]("available")

    override def * = (id.?, name, description, duration.?, uploadDate, available).mapTo[Film]

  }

  class FilmXGenre(tag: Tag) extends Table[(Long, Genre)](tag, "films_x_genre") {

    def filmId = column[Long]("film_id")
    def genre  = column[Genre]("genre")

    def pk = primaryKey("pk_films_genres", (filmId, genre))

    def filmFk =
      foreignKey("fk_film", filmId, table)(
        _.id,
        onUpdate = ForeignKeyAction.Restrict,
        onDelete = ForeignKeyAction.Cascade
      )

    def genreFk =
      foreignKey("fk_genre", genre, genreRepository.table)(
        identity,
        onUpdate = ForeignKeyAction.Restrict,
        onDelete = ForeignKeyAction.Cascade
      )

    override def * = (filmId, genre)

  }
  // scalastyle:on
  // $COVERAGE-ON$

}
