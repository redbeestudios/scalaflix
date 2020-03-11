package repositories

import com.google.inject.Singleton
import converters._
import domain.Genre
import globals.{ApplicationResult, MapMarkerContext}
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import repositories.tables.GenreTable

import scala.concurrent.ExecutionContext

trait GenreRepository extends BaseRepository[Genre] {

  def list(implicit mapMarkerContext: MapMarkerContext): ApplicationResult[Seq[Genre]]

}

@Singleton
class GenreRepositoryImpl @Inject()(override val dbConfigProvider: DatabaseConfigProvider)
                                    (implicit ec: ExecutionContext) extends GenreRepository {

  import profile.api._

  def list(implicit mapMarkerContext: MapMarkerContext)
  : ApplicationResult[Seq[Genre]] = db.run(GenreTable.table.result).toApplicationResult()

}
