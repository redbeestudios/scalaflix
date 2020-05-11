import org.scalatest.BeforeAndAfterAll
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import specs.HealthCheckSpec
import specs.services.repositories.FilmRepositorySpec

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Try

class SeedSuite
    extends PlaySpec
    with GuiceOneServerPerSuite
    with ContainersPerSuite
    with BeforeAndAfterAll
    with FilmRepositorySpec
    with HealthCheckSpec {

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

  "FilmRepository" should {
    behave like filmRepositorySpecs()
  }
}
