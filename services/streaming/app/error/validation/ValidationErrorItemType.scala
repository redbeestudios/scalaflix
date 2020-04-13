package error.validation

import java.security.InvalidParameterException

/**
  * Validation error type
  */
sealed trait ValidationErrorItemType

/**
  * Validation error type companion object
  */
object ValidationErrorItemType {

  /**
    * Creates a validation error type from a String value
    * @param validationErrorType string value
    * @return validation error type
    */
  def apply(validationErrorType: String): ValidationErrorItemType =
    validationErrorType match {
      case value if value equals InvalidParam.toString  => InvalidParam
      case value if value equals ParamRequired.toString => ParamRequired
      case value =>
        throw new InvalidParameterException(
          s"$value did not match any ValidationErrorType."
        )
    }
}

/**
  * Invalid Param
  */
case object InvalidParam extends ValidationErrorItemType

/**
  * Param Missing
  */
case object ParamRequired extends ValidationErrorItemType
