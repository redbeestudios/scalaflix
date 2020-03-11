package json

import io.circe.Json
import play.api.libs.ws.BodyReadable

/**
  * Interface to abstract reads calls.
  */
trait Readable {

  /**
    * Implicit conversion for [[play.api.libs.WSClient]] response to circe json.
    *
    * @return A [[play.api.libs.ws.BodyReadable]] instance.
    */
  implicit protected def circeJsonBodyReadable: BodyReadable[Json] = BodyReadable[io.circe.Json] { response =>
    io.circe.parser.parse(response.bodyAsBytes.utf8String) match {
      case Left(decodingFailure) => throw decodingFailure
      case Right(json)           => json
    }
  }

}
