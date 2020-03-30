package domain

import java.time.LocalDateTime

case class Film(
    id: Option[Long] = None,
    name: String,
    description: String,
    duration: Option[Long] = None,
    uploadDate: LocalDateTime = LocalDateTime.now,
    available: Boolean = false)
