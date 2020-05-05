package specs

import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.libs.ws.WSClient
import play.api.test.Helpers._

trait HealthCheckSpec {
  this: PlaySpec with GuiceOneServerPerSuite =>

  def healthCheckTests(): Unit =
    "HealthCheckController" when {
      "received a GET to /healthcheck" should {
        "Return build Info" in {
          val address = s"http://localhost:$port/healthcheck"

          val result = requests.get(address)
          println(result.text()) // scalastyle:ignore

          result.statusCode mustBe OK
        }
      }
    }
}
