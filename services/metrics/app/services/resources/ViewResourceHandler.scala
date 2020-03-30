package services.resources

import java.time.LocalDateTime

import javax.inject.Inject
import models.{FilmId, View}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.repositories.ViewRepository
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
  * View handler for database access.
  *
  * @param dbConfigProvider Slick database provider.
  * @param repository       A [[services.repositories.ViewRepository]] instance.
  * @param ec               An implicit [[scala.concurrent.ExecutionContext]].
  */
class ViewResourceHandler @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider,
    repository: ViewRepository
  )(implicit ec: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {

  /**
    * Retrieves the views per film from the database.
    *
    * @param from An optional date to filter results.
    * @return A future of [[scala.Seq]] with [[models.FilmId]] and the count of views.
    */
  def listFilmsViews(from: Option[LocalDateTime]): Future[Seq[(FilmId, Int)]] = db.run {
    this.repository.listFilmsViews(from)
  }

  /**
    * Saves the entity in the database.
    *
    * @param filmId Film identifier to add a view.
    * @return A future, it fails if there is an error.
    */
  def addView(filmId: FilmId): Future[Unit] = db.run {
    this.repository.save(View(None, filmId, LocalDateTime.now()))
  }

}
