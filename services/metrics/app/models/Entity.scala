package models

/**
  * Abstract entity used to set the common identifier.
  *
  * @tparam ID Identifier type.
  */
trait Entity[ID] {

  /**
    * Entity Identifier.
    * @return The identifier of type [[models.Entity#ID]]
    */
  def id: Option[ID]

}
