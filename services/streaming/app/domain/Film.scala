package domain

case class Film(id: Option[Int],
                name: String,
                description: String,
                genres: List[Genre],
                duration: Option[Long],
                available: Boolean = false)
