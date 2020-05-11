package validation

import cats.data.NonEmptyList
import error.ValidationError
import error.validation.{InvalidParam, ValidationErrorItem}
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.play.PlaySpec
import testing.Stubs

import scala.util.Random

class FilmValidationsSpec extends PlaySpec with BeforeAndAfterEach {
  final private val DESCRIPTION        = "description"
  private val subject: FilmValidations = new Object with FilmValidations

  "validateFilmRequest" when {
    "given film with " + DESCRIPTION + " shorter than 50 characters" should {
      "return validation errors" in {
        // given
        val filmDTO = Stubs.newFilmDTO.copy(description = "ABC")

        // when
        val validationErrors = subject.validateFilmRequest(filmDTO)

        // then
        val expectedErrors =
          Left(ValidationError(
            NonEmptyList(ValidationErrorItem(InvalidParam, DESCRIPTION, "Length: 3 is not greater than 50."), List())))
        validationErrors mustBe expectedErrors
      }
    }

    "given film with " + DESCRIPTION + " larger than 500 characters" should {
      "return validation errors" in {
        // given
        val filmDTO = Stubs.newFilmDTO.copy(description = Random.alphanumeric.take(1000).mkString)

        // when
        val validationErrors = subject.validateFilmRequest(filmDTO)

        // then
        val expectedErrors =
          Left(
            ValidationError(
              NonEmptyList(ValidationErrorItem(InvalidParam, DESCRIPTION, "Length: 1000 is not lesser than 500."),
                           List())))
        validationErrors mustBe expectedErrors
      }
    }

    "given film with " + DESCRIPTION + " with proper length" should {
      "return no errors" in {
        // given
        val filmDTO = Stubs.newFilmDTO

        // when
        val validationErrors = subject.validateFilmRequest(filmDTO)

        // then
        validationErrors mustBe Right(filmDTO)
      }
    }
  }
}
