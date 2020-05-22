package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU17LoadTest extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.png""", """.*.pneg""", """.*.jpg""", """.*.ico""", """.*.jpeg""", """.*.JPG"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_3 = Map(
		"Origin" -> "http://www.dp2.com")

	object Home {
		val home = exec(http("Home")
			.get("/"))
		.pause(11)
	}
	object Login {
		val login = exec(http("Login")
			.get("/login")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(10)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}"))
		.pause(7)
	}
	
	object TopReadBooks {
		val topReadBooks = exec(http("TopReadBooks")
			.get("/books/topRead"))
		.pause(1)
	}
	val scn = scenario("HU17").exec(Home.home,Login.login,
	TopReadBooks.topReadBooks)

	setUp(scn.inject(rampUsers(32500) during (100 seconds))).protocols(httpProtocol) .assertions( //escenario negativo con 35000
       	 global.responseTime.max.lt(5000),    
       	 global.responseTime.mean.lt(1000),
       	 global.successfulRequests.percent.gt(95))
}