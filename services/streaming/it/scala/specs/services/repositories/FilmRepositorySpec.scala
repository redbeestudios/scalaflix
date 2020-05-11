package specs.services.repositories

import java.time.LocalDateTime

import domain.{Film, Genre}
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.db.slick.DatabaseConfigProvider
import services.repositories.FilmRepository
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape
import testing.Stubs

import scala.concurrent.Await
import scala.concurrent.duration._

trait FilmRepositorySpec extends ScalaFutures { this: PlaySpec with GuiceOneServerPerSuite =>
  implicit protected val genreMapper: BaseColumnType[Genre] = MappedColumnType.base[Genre, String](_.value, Genre.apply)
  private val filmTable                                     = TableQuery[FilmTable]
  private val filmXGenreTable                               = TableQuery[FilmXGenre]
  private val genreTable                                    = TableQuery[GenreTable]

  implicit override def patienceConfig: PatienceConfig = PatienceConfig(scaled(10 seconds), scaled(1 second))

  def filmRepositorySpecs(): Unit =
    "save method" when {
      "given a film instance" should {
        "save it to the database" in {
          // given
          val repository             = app.injector.instanceOf(classOf[FilmRepository])
          val databaseConfigProvider = app.injector.instanceOf(classOf[DatabaseConfigProvider])
          val database               = databaseConfigProvider.get[PostgresProfile].db

          val film   = Stubs.newFilm
          val genres = Await.result(database.run(genreTable.result), patienceConfig.timeout).toList

          // when
          val saveProcess = database.run(repository.save(film, genres))

          // then
          whenReady(saveProcess) { savedFilm =>
            val findFilmAction      = filmTable.filter(_.name === film.name).result
            val filmGenres          = filmXGenreTable.filter(_.filmId === savedFilm.id).result
            val findFilmQuery       = Await.result(database.run(findFilmAction), patienceConfig.timeout)
            val findFilmGenresQuery = Await.result(database.run(filmGenres), patienceConfig.timeout)

            findFilmQuery must contain only savedFilm
            findFilmGenresQuery.map(_._2) must contain allElementsOf genres
          }
        }
      }
    }

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
      foreignKey("fk_film", filmId, filmTable)(
        _.id,
        onUpdate = ForeignKeyAction.Restrict,
        onDelete = ForeignKeyAction.Cascade
      )

    def genreFk =
      foreignKey("fk_genre", genre, genreTable)(
        identity,
        onUpdate = ForeignKeyAction.Restrict,
        onDelete = ForeignKeyAction.Cascade
      )

    override def * = (filmId, genre)

  }

  class GenreTable(tag: Tag) extends Table[Genre](tag, "genres") {

    def value: Rep[Genre] = column[Genre]("value", O.PrimaryKey)

    override def * : ProvenShape[Genre] = value

  }
  // scalastyle:on
  // $COVERAGE-ON$
}
