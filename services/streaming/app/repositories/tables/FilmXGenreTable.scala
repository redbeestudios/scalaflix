package repositories.tables

import slick.jdbc.PostgresProfile.api._
import slick.lifted.{PrimaryKey, ProvenShape, TableQuery}

class FilmXGenreTable(tag: Tag) extends Table[(Int, String)](tag, "films_x_genre") {

  def filmId: Rep[Int]   = column[Int]("film_id")
  def genre: Rep[String] = column[String]("genre")
  def pk: PrimaryKey     = primaryKey("pk_films_genres", (filmId, genre))

  def filmFk =
    foreignKey("fk_film", filmId, FilmTable.table)(_.id,
                                                   onUpdate = ForeignKeyAction.Restrict,
                                                   onDelete = ForeignKeyAction.Cascade)

  def genreFk =
    foreignKey("fk_genre", genre, GenreTable.table)(_.value,
                                                    onUpdate = ForeignKeyAction.Restrict,
                                                    onDelete = ForeignKeyAction.Cascade)

  override def * : ProvenShape[(Int, String)] = (filmId, genre)
}

object FilmXGenreTable {
  val table = TableQuery[FilmXGenreTable]
}
