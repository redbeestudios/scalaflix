package services.repositories

import java.time.LocalDateTime

import javax.inject.{Inject, Named, Singleton}
import models.config._
import models.{FilmId, View, ViewId}
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.ExecutionContext

/**
  * Metrics repository.
  *
  * @param dbConfigProvider Slick database provider.
  * @param ec               An implicit [[scala.concurrent.ExecutionContext]].
  */
@Singleton
class ViewRepository @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider
  )(implicit @Named(DATABASE_DISPATCHER) ec: ExecutionContext)
    extends Repository[ViewId, View] {

  import profile.api._

  override val idColumn: BaseColumnType[ViewId] = viewIdColumn

  override protected type EntityTableType = Views
  override val table: TableQuery[EntityTableType] = TableQuery[Views]

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

  // $COVERAGE-OFF$
  // scalastyle:off
  class Views(tag: Tag) extends EntityTable(tag, "views") {

    def filmId   = column[FilmId]("film_id")
    def datetime = column[LocalDateTime]("datetime")

    override def * = (id.?, filmId, datetime).mapTo[View]

  }
  // scalastyle:on
  // $COVERAGE-ON$

}
