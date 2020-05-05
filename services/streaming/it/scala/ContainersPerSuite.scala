import java.io.File
import java.net.Socket

import org.scalatestplus.play.PlaySpec

import scala.util.{Failure, Success, Try}
import sys.process._

trait ContainersPerSuite { this: PlaySpec =>

  val minioPort                        = 9002
  var minioContainerId: Option[String] = None

  val postgresPort                        = 5432
  var postgresContainerId: Option[String] = None

  val metricsPort                        = 9090
  var metricsContainerId: Option[String] = None

  def startContainers(): Unit = {
    val sqlScript = new File("./sql/create-databases.sql").getAbsolutePath

    minioContainerId = startMinioContainer
    postgresContainerId = startPostgresContainer(sqlScript)
    metricsContainerId = startMetricsContainer

    val attempts   = 200
    val STARTED    = "Started"
    val FAILED     = "Failed"
    var counter    = attempts
    var minioOk    = false
    var postgresOk = false

    while (counter > 0) {
      Try {
        println(s"Checking services, attempt: ${attempts - (counter - 1)}") // scalastyle:ignore

        new Socket("localhost", 9002)
        minioOk = true

        new Socket("localhost", 5432)
        postgresOk = true
      } match {
        case Failure(_) => counter -= 1
        case Success(_) => counter = 0
      }
    }

    if (!minioOk || !postgresOk) {
      fail(
        "Docker compose could not start properly. Status:\n" +
          s"Minio: ${if (minioOk) STARTED else FAILED}" +
          s"Postgres: ${if (postgresOk) STARTED else FAILED}"
      )
    }
  }

  def stopContainers(): Unit = {
    minioContainerId.foreach(containerId => s"docker stop $containerId" !!)
    postgresContainerId.foreach(containerId => s"docker stop $containerId" !!)
    metricsContainerId.foreach(containerId => s"docker stop $containerId" !!)
  }

  private def startPostgresContainer(sqlScript: String) =
    Option(
      s"docker run -d --rm -p $postgresPort:5432 " +
        s"-e POSTGRES_USER=postgres " +
        s"-e POSTGRES_PASSWORD=postgres " +
        s"-v $sqlScript:/docker-entrypoint-initdb.d/create-databases.sql postgres:12-alpine" !!
    )

  private def startMinioContainer =
    Option(
      s"docker run -d --rm -p $minioPort:9000 " +
        s"-e MINIO_ACCESS_KEY=minio " +
        s"-e MINIO_SECRET_KEY=minioadmin " +
        s"minio/minio server /data" !!
    )

  private def startMetricsContainer = Option(s"docker run -d --rm -p $metricsPort:1080 mockserver/mockserver" !!)

}
