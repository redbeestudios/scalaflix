package controllers

import controllers.circe.CirceImplicits
import domain.requests.FilmDTO
import globals.MapMarkerContext
import io.circe.parser.decode
import org.mockito.ArgumentMatchers.{eq => meq, _}
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import play.api.test.Helpers._
import play.api.test._
import services.{FilmService, MetricsService}
import testing.Stubs

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

class FilmControllerSpec extends PlaySpec with MockitoSugar with BeforeAndAfterEach with CirceImplicits {

  private var filmService: FilmService       = _
  private var metricsService: MetricsService = _
  private val controllerComponents           = stubControllerComponents()

  private var subject: FilmController = _

  override protected def beforeEach(): Unit = {
    filmService = mock[FilmService]
    metricsService = mock[MetricsService]

    subject = new FilmController(filmService, metricsService, controllerComponents)
  }

  "createFilm" when {
    "asked to create a new Film" should {
      "add it to the system" in {
        // given
        val filmDTO = Stubs.newFilmDTO
        val request = FakeRequest().withBody(filmDTO)

        val addedFilm = filmDTO.copy(id = Some(1))

        when(filmService.save(meq(filmDTO.copy(id = None)))(any[MapMarkerContext]))
          .thenReturn(Future.successful(Right(addedFilm)))

        // when
        val result = subject.createFilm()(request)

        // then
        status(result) mustBe CREATED
        val createdFilm = decode[FilmDTO](contentAsString(result)).right.get
        createdFilm mustBe addedFilm
      }
    }
  }

}
