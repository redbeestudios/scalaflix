package validation

import cats.data.Validated._
import cats.data.ValidatedNel
import cats.implicits._
import domain.Genre
import domain.requests.FilmRequest
import error.{ApplicationError, NotFoundError}
import converters._
import error.validation.{InvalidParam, ValidationErrorItem}
import globals.{ValidationResult, VideoFile}
import play.api.mvc.MultipartFormData
import play.api.libs.Files

trait FilmValidations extends Validations {

  private def key = "film"

  def validateVideoFileHeader(oVideoFile: Option[VideoFile]): ValidationResult[VideoFile] =
    oVideoFile match {
      case Some(videoFile) => Right(videoFile)
      case None            => Left(NotFoundError(key, None, "Could not find film header."))
    }

  def validateFilmRequest(filmRequest: FilmRequest): ValidationResult[FilmRequest] = {

    val validationErrorItems = validateDescription(filmRequest.description) |+|
      validateName(filmRequest.name) |+|
      validateGenres(filmRequest.genres)

    validationErrorItems.toValidationResult(filmRequest)
  }

  private def validateDescription(description: String): ValidatedNel[ValidationErrorItem, Unit] =
    validateLength(50, 500)(description).leftMap(_.toValidationErrorItems(InvalidParam, "description"))

  private def validateName(name: String): ValidatedNel[ValidationErrorItem, Unit] =
    validateLength(1, 100)(name).leftMap(_.toValidationErrorItems(InvalidParam, "name"))

  private def validateGenres(genres: List[Genre]): ValidatedNel[ValidationErrorItem, Unit] =
    if (genres.nonEmpty) invalidNel(
        ValidationErrorItem(InvalidParam, "genre", "There must be at least one genre.")
    )
    else Valid(())
}
