package error.validation

/**
* Error representation for each field.
*
* @param code  Error type.
* @param param Property with the error.
* @param description Description of the error.
*/
case class ValidationErrorItem(code: ValidationErrorItemType, param: String, description: String)
