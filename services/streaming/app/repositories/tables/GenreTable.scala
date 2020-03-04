package repositories.tables

import domain.Genre
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, TableQuery}

class GenreTable(tag: Tag) extends Table[Genre](tag, "genres") {

  def value: Rep[String] = column[String]("value", O.PrimaryKey)

  override def * : ProvenShape[Genre] = value.<>(Genre.apply, Genre.unapply)
}

object GenreTable {
  val table = TableQuery[GenreTable]
}
