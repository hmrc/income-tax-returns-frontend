package uk.gov.hmrc.incometaxreturnsfrontend

import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import scala.concurrent.ExecutionContext.Implicits.global
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{StringContextOps, HttpReads, HeaderCarrier}
import uk.gov.hmrc.http.HttpReads.Implicits.readRaw
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.ws.WSClient
import play.api.test.Helpers._

class HealthEndpointIntegrationSpec
  extends AnyWordSpec
     with Matchers
     with ScalaFutures
     with IntegrationPatience
     with GuiceOneServerPerSuite{

private val baseUrl = s"http://localhost:$port"
implicit val ws: WSClient = app.injector.instanceOf[WSClient]

  override def fakeApplication(): Application =
    GuiceApplicationBuilder()
      .build()

  "service health endpoint" should {
    "respond with 200 status" in {
      val response = await(
        ws.url(s"$baseUrl/ping/ping")
          .get()
      )

      response.status shouldBe 200
    }
  }
}
