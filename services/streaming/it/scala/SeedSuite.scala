import org.scalatest.BeforeAndAfterAll
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import specs.{FilmSpec, HealthCheckSpec}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Try

class SeedSuite
    extends PlaySpec
    with GuiceOneServerPerSuite
    with ContainersPerSuite
    with BeforeAndAfterAll
    with HealthCheckSpec
    with FilmSpec {

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    startContainers()
  }

  override protected def afterAll(): Unit = {
    super.afterAll()
    Try(Await.ready(runningServer.app.stop(), 20 seconds))
    stopContainers()
  }

  "Health check" should {
    behave like healthCheckTests()
  }

  "Films API" should {
    behave like filmsRetrievalSpecs()
  }
}
