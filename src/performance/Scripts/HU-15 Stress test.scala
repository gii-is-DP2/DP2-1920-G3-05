
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU15StressTest extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.jpg""", """.*.jpeg""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_2 = Map("Origin" -> "http://www.dp2.com")

	object Home{
		val home = exec(http("Home")
		.get("/"))
		.pause(12)
	}

	object LoginNewsReview {
		val loginNewsReview = exec(http("LoginNewsReview")
			.get("/login")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(12)
		.exec(http("LoggedNewsReview")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}"))
		.pause(10)
		

	}

		object LoginNewsNoReview {
		val loginNewsNoReview = exec(http("LoginNewsNoReview")
			.get("/login")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(12)
		.exec(http("LoggedNewsNoReview")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "reader2")
			.formParam("password", "reader")
			.formParam("_csrf", "${stoken}"))
		.pause(10)
		

	}

	val ReviewNewsScenario = scenario("NewsReview").exec(Home.home,
													LoginNewsReview.loginNewsReview)

	val NoReviewNewsScenario = scenario("NewsNoReview").exec(Home.home,
													LoginNewsNoReview.loginNewsNoReview)

	setUp(ReviewNewsScenario.inject(rampUsers(8000) during (10 seconds)),
			NoReviewNewsScenario.inject(rampUsers(8000) during (10 seconds)))
		.protocols(httpProtocol)
	


	
}