package domain.requests

import domain.{Film, Genre}

case class FilmRequest(name: String, description: String, genres: List[Genre]) extends DomainRequest[Film] {

  def toDomain: Film = Film(name = name, description = description, genres = genres)

}
