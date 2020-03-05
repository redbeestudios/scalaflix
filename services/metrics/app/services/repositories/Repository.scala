package services.repositories

import models.{Entity, FilmId, ViewId}
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

/**
  * Base repository.
  *
  * @param ec An implicit [[scala.concurrent.ExecutionContext]].
  * @tparam ID             Identifier type.
  * @tparam ConcreteEntity Entity type.
  */
abstract class Repository[ID, ConcreteEntity <: Entity[ID]](implicit ec: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  /**
    * Implicit id [[slick.relational.RelationalProfile#API#MappedColumnType]].
    *
    * @return A [[slick.relational.RelationalProfile#API#BaseColumnType]].
    */
  implicit protected val idColumn: BaseColumnType[ID]

  /**
    * Table type.
    */
  protected type EntityTableType <: EntityTable

  /**
    * Table Query.
    *
    * @return A [[slick.lifted.Aliases#TableQuery]] instance.
    */
  def table: TableQuery[EntityTableType]

  /**
    * Retrieve all elements from the database.
    *
    * @return An action to retrieve all the elements.
    */
  def list: DBIO[Seq[ConcreteEntity]] = this.table.result

  /**
    * Tries to retrieve the element by its id.
    *
    * @param id Entity identifier.
    * @return An action that returns an [[scala.Option]], being Some if exists in the database, None otherwise.
    */
  def get(id: ID): DBIO[Option[ConcreteEntity]] = this.table.filter(_.id === id).result.headOption

  /**
    * Saves the entity in the database. Creates an insert if the id doesn't exists, updates the value otherwise.
    *
    * @param data Entity to be persisted.
    * @return An action, it fails if there is an error.
    */
  def save(data: ConcreteEntity): DBIO[Unit] = this.table.insertOrUpdate(data).map(_ => ())

  implicit protected val viewIdColumn: BaseColumnType[ViewId] =
    MappedColumnType.base[ViewId, Long](
      _.value,
      ViewId.apply
    )

  implicit protected val filmIdColumn: BaseColumnType[FilmId] =
    MappedColumnType.base[FilmId, Long](
      _.value,
      FilmId.apply
    )

  // scalastyle:off
  abstract class EntityTable(tag: Tag, tableName: String, schemaName: Option[String] = None)
      extends Table[ConcreteEntity](tag, schemaName, tableName) {

    def id: Rep[ID] = column[ID]("id", O.PrimaryKey, O.AutoInc)

  }
  // scalastyle:on

}
