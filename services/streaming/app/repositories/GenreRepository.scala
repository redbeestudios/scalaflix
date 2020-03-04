package repositories

import com.google.inject.Singleton
import domain.Genre
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import repositories.tables.GenreTable

import scala.concurrent.Future

trait GenreRepository extends BaseRepository[Genre] {

  def list: Future[Seq[Genre]]

}

@Singleton
class GenreRepositoryImpl @Inject()(override val dbConfigProvider: DatabaseConfigProvider) extends GenreRepository {

  import profile.api._

  def list: Future[Seq[Genre]] = db.run(GenreTable.table.result)

}
