import specs.HealthCheckSpec
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite

class SeedSuite extends PlaySpec with HealthCheckSpec with GuiceOneServerPerSuite {
  "healthCheck" should {
    behave like healtCheckTests(app, port)
  }
}
