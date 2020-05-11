package converters.result

import akka.util.ByteString
import cats.data.NonEmptyList
import controllers.circe.CirceImplicits
import domain.requests.FilmDTO
import error.validation.{InvalidParam, ValidationErrorItem}
import error.{ExecutionError, NotFoundError, ValidationError}
import io.circe.Json
import io.circe.syntax._
import org.scalatest.{AsyncWordSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures
import play.api.http.MimeTypes
import play.api.mvc.Results

import scala.concurrent.Future
import scala.util.Random

class ApplicationResultConvertersSpec
    extends AsyncWordSpec
    with Results
    with Matchers
    with CirceImplicits
    with ApplicationResultConverters
    with ScalaFutures {
  implicit private def circeWritable: play.api.http.Writeable[Json] =
    new play.api.http.Writeable[Json](json => ByteString(json.toString()), Some(MimeTypes.JSON))

  "handleApplicationError" when {
    "given a ExecutionError" should {
      "return InternalServerError with the error as json" in {
        // given
        val executionError    = ExecutionError(Random.alphanumeric.take(10).mkString)
        val applicationResult = Future.successful(Left[ExecutionError, FilmDTO](executionError))

        // when
        val result = applicationResult.toCreatedResult

        // given
        result.map(_ shouldBe InternalServerError(executionError.asJson))
      }
    }

    "given a ValidationError" should {
      "return ValidationError with the error as json" in {
        // given
        val validationError = ValidationError(
          NonEmptyList(ValidationErrorItem(InvalidParam, "param", Random.alphanumeric.take(10).mkString), List())
        )
        val applicationResult = Future.successful(Left[ValidationError, FilmDTO](validationError))

        // when
        val result = applicationResult.toCreatedResult

        // given
        result.map(_ shouldBe BadRequest(validationError.asJson))
      }
    }

    "given a NotFound" should {
      "return NotFound with the error as json" in {
        // given
        val notFoundError     = NotFoundError("film", Some("1"), """Film with id: "1" was not found""")
        val applicationResult = Future.successful(Left[NotFoundError, FilmDTO](notFoundError))

        // when
        val result = applicationResult.toCreatedResult

        // given
        result.map(_ shouldBe NotFound(notFoundError.asJson))
      }
    }
  }
}
