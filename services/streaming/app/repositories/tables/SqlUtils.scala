package repositories.tables

import java.sql.{Date, Timestamp}
import java.time.LocalDateTime

trait SqlUtils {

  def toLocalDateTime(date: Date): LocalDateTime = {
    val timestamp = new Timestamp(date.getTime)
    timestamp.toLocalDateTime
  }

  def toSqlDate(dateTime: LocalDateTime): Date =
    Date.valueOf(dateTime.toLocalDate)
}
