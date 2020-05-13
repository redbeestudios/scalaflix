package services

import configurations.MetricsConfiguration
import org.mockito.Mockito._
import org.mockito.invocation.InvocationOnMock
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import testing.Stubs

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

class MetricsServiceSpec extends PlaySpec with MockitoSugar with ScalaFutures {
  "addView" when {
    "given id of the opened film" should {
      "make the correct API call to the Metrics micro-service and increases its views count" in {
        // given
        val host = "localhost"
        val port = "9002"

        val wsClient   = mock[WSClient]
        val wsRequest  = mock[WSRequest]
        val wsResponse = mock[WSResponse]

        val configuration           = new MetricsConfiguration(host, port)
        val subject                 = new MetricsService(wsClient, configuration)
        var calledWSClientCorrectly = false

        val film = Stubs.newFilm.copy(id = Some(1))
        val url  = s"${configuration.host}:${configuration.port}/metrics/${film.id.get}/views"

        when(wsClient.url(url)).thenReturn(wsRequest)
        when(wsRequest.put("")).thenAnswer((_: InvocationOnMock) => {
          calledWSClientCorrectly = true
          Future.successful(wsResponse)
        })
        when(wsResponse.status).thenReturn(204)

        // when
        val operation = subject.addView(film.id.get)

        // then
        whenReady(operation)(_ => calledWSClientCorrectly mustBe true)
      }
    }
  }
}
