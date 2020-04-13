/**
  * Constants for configurations keys.
  */
package object configurations {

  private val prefix = "streaming"

  private val minioPrefix = s"$prefix.minio"
  val MINIO_ENDPOINT      = s"$minioPrefix.endpoint"
  val MINIO_ACCESS_KEY    = s"$minioPrefix.access-key"
  val MINIO_SECRET_KEY    = s"$minioPrefix.secret-key"

  // $COVERAGE-OFF$ # a final val can't be taken by coverage tools
  final val EXTERNAL_DISPATCHER = "dispatchers.external" // final because is used in annotations
  // $COVERAGE-ON$

  private val metricsPrefix = s"$prefix.metrics"

  val METRICS_HOST = s"$metricsPrefix.host"

  val METRICS_PORT = s"$metricsPrefix.port"

}
