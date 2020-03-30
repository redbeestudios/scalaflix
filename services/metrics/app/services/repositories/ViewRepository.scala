package services.repositories

import java.time.LocalDateTime

import javax.inject.{Inject, Singleton}
import models.{FilmId, View, ViewId}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

/**
  * Metrics repository.
  *
  * @param dbConfigProvider Slick database provider.
  * @param ec               An implicit [[scala.concurrent.ExecutionContext]].
  */
@Singleton
class ViewRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  implicit protected val viewIdColumn: BaseColumnType[ViewId] =
    MappedColumnType.base[ViewId, Long](
      _.value,
      ViewId.apply
    )

  implicit protected val filmIdColumn: BaseColumnType[FilmId] =
    MappedColumnType.base[FilmId, Long](
      _.value,
      FilmId.apply
    )

  val table = TableQuery[ViewTable]

  /**
    * Generates a [[scala.Seq]] with views per film.
    *
    * @param from An optional date to filter results.
    * @return A [[scala.Seq]] with [[models.FilmId]] and the count of views.
    */
  def listFilmsViews(from: Option[LocalDateTime]): DBIO[Seq[(FilmId, Int)]] = {
    val query = from match {
      case None       => this.table
      case Some(date) => this.table.filter(_.datetime >= date)
    }

    query
      .groupBy(_.filmId)
      .map {
        case (filmId, viewsQuery) => filmId -> viewsQuery.length
      }
      .result
  }

  /**
    * Saves the entity in the database. Creates an insert if the id doesn't exists, updates the value otherwise.
    *
    * @param view Entity to be persisted.
    * @return An action, it fails if there is an error.
    */
  def save(view: View): DBIO[Unit] = this.table.insertOrUpdate(view).map(_ => ())

  // $COVERAGE-OFF$
  // scalastyle:off
  class ViewTable(tag: Tag) extends Table[View](tag, "views") {

    def id       = column[ViewId]("id", O.PrimaryKey, O.AutoInc)
    def filmId   = column[FilmId]("film_id")
    def datetime = column[LocalDateTime]("datetime")

    override def * = (id.?, filmId, datetime).mapTo[View]

  }
  // scalastyle:on
  // $COVERAGE-ON$

}
