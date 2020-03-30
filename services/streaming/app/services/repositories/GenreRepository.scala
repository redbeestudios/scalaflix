package services.repositories

import com.google.inject.Singleton
import domain.Genre
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.lifted.ProvenShape

import scala.concurrent.Future

@Singleton
class GenreRepository @Inject()(override val dbConfigProvider: DatabaseConfigProvider) extends BaseRepository {

  import profile.api._

  val table = TableQuery[GenreTable]

  def list: Future[Seq[Genre]] = db.run(this.table.result)

  class GenreTable(tag: Tag) extends Table[Genre](tag, "genres") {

    def value: Rep[Genre] = column[Genre]("value", O.PrimaryKey)

    override def * : ProvenShape[Genre] = value

  }

}
