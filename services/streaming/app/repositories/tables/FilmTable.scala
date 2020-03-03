package repositories.tables

import java.sql.Date

import domain.Film
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, TableQuery}

class FilmTable(tag: Tag) extends Table[Film](tag, "films") with SqlUtils {

  def id: Rep[Int]             = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name: Rep[String]        = column[String]("name")
  def description: Rep[String] = column[String]("description")
  def duration: Rep[Int]       = column[Int]("duration")
  def uploadDate: Rep[Date]    = column[Date]("uploadDate")
  def views: Rep[Long]         = column[Long]("views")
  def available: Rep[Boolean]  = column[Boolean]("available")

  override def * : ProvenShape[Film] =
    (id.?, name, description, duration.?, uploadDate, views, available).<>(
      {
        case (id, name, description, duration, uploadDate, views, available) =>
          Film(
            id = id,
            name = name,
            description = description,
            genres = Nil,
            duration = duration,
            uploadDate = toLocalDateTime(uploadDate),
            views = views,
            available = available
          )
      }, { film: Film =>
        Some(
          (
            film.id,
            film.name,
            film.description,
            film.duration,
            toSqlDate(film.uploadDate),
            film.views,
            film.available
          ))
      }
    )
}

object FilmTable {
  val table = TableQuery[FilmTable]
}
