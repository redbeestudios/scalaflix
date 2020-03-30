import converters.future._
import converters.result._
import converters.validation._
import play.api.mvc.PlayBodyParsers

package object converters
    extends ValidationErrorItemsConverters
    with ErrorDescriptionConverters
    with ValidationResultConverters
    with ApplicationResultConverters
    with FutureConverters
