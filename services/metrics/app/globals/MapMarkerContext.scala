package globals

import models.ViewId
import net.logstash.logback.marker.{LogstashMarker, Markers}
import play.api.MarkerContext

import scala.collection.JavaConverters._
import scala.collection.mutable

/**
  * This implementation of MarkerContext behaves like a mutable map,
  * allowing the user to insert, update and remove its stored values.
  *
  * {{{
  *   val map = mutable.Map(
  *     "key" -> value
  *   )
  *   implicit val mmc: MapMarkerContext = MapMarkerContext(map)
  *   log.info("This message will be logged with the MapMarkerContext marker")
  * }}}
  *
  * @param map is the mutable map
  */
case class MapMarkerContext(private val map: mutable.Map[String, String] = mutable.Map()) extends MarkerContext {

  /**
    * Converts its internal map into an Option of LogstashMarker.
    *
    * @return an [[scala.Option]] of [[net.logstash.logback.marker.LogstashMarker]]
    */
  def marker: Option[LogstashMarker] = Option(Markers.appendEntries(map.asJava))

  /**
    * Gets the value from the provided key.
    *
    * @param key The key to get
    * @return the value from the provided key
    */
  def apply(key: String): Option[String] = map.get(key)

  /**
    * Adds a new key/value pair to this MapMarkerContext.
    * If the map already contains a mapping for the key,
    * it will be overridden by the new value.
    *
    * @param key The key to update
    * @param value The new value
    */
  def update(key: String, value: String): Unit = map.update(key, value)

  /**
    * Adds a new key/value pair to this MapMarkerContext.
    * If the map already contains a mapping for the key,
    * it will be overridden by the new value.
    *
    * @param kv the key/value pair.
    * @return the MapMarkerContext itself
    */
  def +=(kv: (String, String)): MapMarkerContext = {
    map += kv
    this
  }

  /**
    * Removes a key from this MapMarkerContext.
    *
    * @param key the key to be removed
    * @return the MapMarkerContext itself.
    */
  def -=(key: String): MapMarkerContext = {
    map -= key
    this
  }

}

/**
  * Map Marker Context companion object.
  */
object MapMarkerContext {

  /**
    * Converts a metric id into a MapMarkerContext.
    *
    * @param metricId The metric identifier.
    * @return A new instance of [[globals.MapMarkerContext]].
    */
  def apply(metricId: ViewId): MapMarkerContext = {
    val map = mutable.Map(
      "metric_id" -> metricId.value.toString
    )
    MapMarkerContext(map)
  }
}
