package error

case class NotFoundError(entity: String, id: Option[String], description: String) extends ApplicationError
