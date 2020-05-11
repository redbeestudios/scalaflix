package testing

import domain.Genre
import domain.requests.FilmDTO

object Stubs {

  def newFilmDTO: FilmDTO = FilmDTO(
    id = Some(1),
    name = "Night's fall",
    description = "Wonderful movie. Everybody should see it with a bunch of pop corn in their hands. It's a " +
      "must not miss",
    genres = List(Genre("drama"))
  )
}
