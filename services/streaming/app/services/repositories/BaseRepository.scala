package services.repositories

import domain.Genre
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

/**
  * Base Repository trait
  */
trait BaseRepository extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  implicit protected val genreMapper: BaseColumnType[Genre] =
    MappedColumnType.base[Genre, String](
      _.value,
      Genre.apply
    )

}
