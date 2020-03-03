package repositories.tables

import slick.jdbc.PostgresProfile.api._
import slick.lifted.{PrimaryKey, ProvenShape, TableQuery}

class FilmXGenreTable(tag: Tag) extends Table[(Int, Int)](tag, "films_x_genre") {

  def filmId: Rep[Int]  = column[Int]("film_id")
  def genreId: Rep[Int] = column[Int]("genre_id")
  def pk: PrimaryKey    = primaryKey("pk_films_genres", (filmId, genreId))

  override def * : ProvenShape[(Int, Int)] = (filmId, genreId)
}

object FilmXGenreTable {
  val table = TableQuery[FilmXGenreTable]
}
