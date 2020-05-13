package testing

import domain.requests.FilmDTO
import domain.{Film, Genre}

object Stubs {

  def newFilm: Film = Film(
    name = "Night's fall",
    description = "Wonderful movie. Everybody should see it with a bunch of pop corn in their hands. It's a " +
      "must not miss",
    duration = Some(1000),
    available = true
  )

  def newFilmDTO: FilmDTO = FilmDTO(
    id = Some(1),
    name = "Night's fall",
    description = "Wonderful movie. Everybody should see it with a bunch of pop corn in their hands. It's a " +
      "must not miss",
    genres = List(Genre("drama"))
  )
}
