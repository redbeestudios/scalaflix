package specs

import org.scalatestplus.play.PlaySpec
//import requests.get

trait FilmSpec { this: PlaySpec =>
//  private val streamingApi = "http://localhost:9000/films"

  def filmsRetrievalSpecs(): Unit =
    "" when {
      "asked for all the films of certain genres in the system" should {
        "give them all to the requesting client" in {
          // given
          // include initially videos of different genres
//          val videos = Array("a").map(fileName => {
//            val filePath = ""
//          })

          // when
//          val response = get(s"$streamingApi?genre=comedy")
        }
      }
    }
}
