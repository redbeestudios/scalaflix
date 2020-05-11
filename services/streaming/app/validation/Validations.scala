package validation

import cats.data.Validated._
import cats.data.ValidatedNel
import cats.implicits._

trait Validations {

  private val lengthPrefix = "Length: "
  private val valuePrefix  = "Value: "

  def validateMaximumLength(maximumLength: Int)(value: String): ValidatedNel[String, Unit] =
    validateGreaterThan(maximumLength: Int)(value.length, lengthPrefix)

  def validateMinimumLength(maximumLength: Long)(value: String): ValidatedNel[String, Unit] =
    validateLessThan(maximumLength)(value.length, lengthPrefix)

  def validateLength(minimumLength: Long, maximumLength: Long)(value: String): ValidatedNel[String, Unit] =
    validateBetween(minimumLength, maximumLength)(value.length, lengthPrefix)

  def validateGreaterThan(
      minimumValue: Long
    )(value: Long,
      descriptionPrefix: String = valuePrefix
    ): ValidatedNel[String, Unit] =
    if (value <= minimumValue) {
      invalidNel(s"$descriptionPrefix$value is not greater than $minimumValue.")
    } else validNel(())

  def validateLessThan(
      maximumValue: Long
    )(value: Long,
      descriptionPrefix: String = valuePrefix
    ): ValidatedNel[String, Unit] =
    if (value >= maximumValue) {
      invalidNel(s"$descriptionPrefix$value is not lesser than $maximumValue.")
    } else validNel(())

  def validateBetween(
      minimumValue: Long,
      maximumValue: Long
    )(value: Long,
      descriptionPrefix: String = valuePrefix
    ): ValidatedNel[String, Unit] =
    validateGreaterThan(minimumValue)(value, descriptionPrefix) |+| validateLessThan(maximumValue)(value,
                                                                                                   descriptionPrefix)
}
