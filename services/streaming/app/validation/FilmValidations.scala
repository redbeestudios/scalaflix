package validation

import cats.data.Validated._
import cats.data.ValidatedNel
import cats.implicits._
import converters._
import domain.Genre
import domain.requests.FilmDTO
import error.NotFoundError
import error.validation.{InvalidParam, ValidationErrorItem}
import globals.{ValidationResult, VideoFile}

trait FilmValidations extends Validations {

  private def key = "film"

  def validateVideoFileHeader(oVideoFile: Option[VideoFile]): ValidationResult[VideoFile] =
    oVideoFile match {
      case Some(videoFile) => Right(videoFile)
      case None            => Left(NotFoundError(key, None, "Could not find film header."))
    }

  def validateFilmRequest(filmDTO: FilmDTO): ValidationResult[FilmDTO] = ???

  private def validateDescription(description: String): ValidatedNel[ValidationErrorItem, Unit] =
    validateLength(50, 500)(description)
      .leftMap(_.toValidationErrorItems(InvalidParam, "description"))

  private def validateName(name: String): ValidatedNel[ValidationErrorItem, Unit] =
    validateLength(1, 100)(name)
      .leftMap(_.toValidationErrorItems(InvalidParam, "name"))

  private def validateGenres(genres: List[Genre]): ValidatedNel[ValidationErrorItem, Unit] =
    if (genres.isEmpty) {
      invalidNel(ValidationErrorItem(InvalidParam, "genre", "There must be at least one genre."))
    } else {
      genres
        .map(genre => validateLength(1, 50)(genre.value))
        .fold(Valid())(_ |+| _)
        .leftMap(errors => errors.map(error => ValidationErrorItem(InvalidParam, "genre", error)))
    }
}
