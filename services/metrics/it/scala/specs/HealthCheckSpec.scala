package specs

import org.scalatestplus.play._
import play.api.Application
import play.api.libs.ws.WSClient
import play.api.test.Helpers._

trait HealthCheckSpec { this: PlaySpec =>

  def healtCheckTests(app: Application, port: Int): Unit = {
    "healthCheckController GET" should {
      "Return build Info" in {
        val wsClient = app.injector.instanceOf[WSClient]
        val address  = s"http://localhost:$port/healthcheck"

        val result = await(wsClient.url(address).get())

        result.status mustBe OK
      }
    }
  }
}
