package domain.requests

import domain.Genre

case class FilmDTO(id: Option[Long], name: String, description: String, genres: List[Genre])
