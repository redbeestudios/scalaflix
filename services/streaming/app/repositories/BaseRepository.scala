package repositories

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

/**
  * Base Repository trait
  *
  * @tparam T Type of the repository
  */
trait BaseRepository[T] extends HasDatabaseConfigProvider[JdbcProfile] {

  val dbConfigProvider: DatabaseConfigProvider
}
