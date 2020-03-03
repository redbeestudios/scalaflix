package repositories.tables

import domain.Genre
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, TableQuery}

class GenreTable(tag: Tag) extends Table[Genre](tag, "genres") {

  def id: Rep[Int]       = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def value: Rep[String] = column[String]("value")

  override def * : ProvenShape[Genre] = (id.?, value).<>(Genre.tupled, Genre.unapply)
}

object GenreTable {
  val table = TableQuery[GenreTable]
}
