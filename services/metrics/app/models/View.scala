package models

import java.time.LocalDateTime

/**
  * Metric representation.
  *
  * @param id       View identifier.
  * @param filmId   Associated film identifier.
  * @param datetime View datetime.
  */
case class View(id: Option[ViewId], filmId: FilmId, datetime: LocalDateTime)

/**
  * Value class for the film identifier.
  *
  * @param value The film id to wrap.
  */
case class ViewId(value: Long) extends AnyVal

/**
  * Value class for the film identifier.
  *
  * @param value The film id to wrap.
  */
case class FilmId(value: Long) extends AnyVal
