package services.repositories

import com.google.inject.Singleton
import domain.Genre
import globals.{ApplicationResult, MapMarkerContext}
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.lifted.ProvenShape
import converters._

import scala.concurrent.ExecutionContext

@Singleton
class GenreRepository @Inject()(override val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
    extends BaseRepository {

  import profile.api._

  val table = TableQuery[GenreTable]

  def list(implicit mapMarkerContext: MapMarkerContext): ApplicationResult[Seq[Genre]] =
    db.run(this.table.result).toApplicationResult()

  class GenreTable(tag: Tag) extends Table[Genre](tag, "genres") {

    def value: Rep[Genre] = column[Genre]("value", O.PrimaryKey)

    override def * : ProvenShape[Genre] = value

  }

}
