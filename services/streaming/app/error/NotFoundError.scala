package error

case class NotFoundError(entity: String, id: String, description: String) extends ApplicationError
