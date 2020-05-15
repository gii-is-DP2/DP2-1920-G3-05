package itApi

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class TwoScenarios extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.png""", """.*.pneg""", """.*.jpg""", """.*.ico""", """.*.jpeg"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0")

	val headers_2 = Map("Origin" -> "http://www.dp2.com")

	object Home {
		val home = exec(http("HOME")
			.get("/"))
		.pause(6)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(20)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}"))
		.pause(7)
	}

	object SearchItBookForm {
		val searchItBookForm = exec(http("SearchItBookForm")
			.get("/itBooks/find"))
		.pause(19)
	}

	object SearchItBooks {
		val searchItBooks = exec(http("searchItBooks")
			.get("/itBooks?title=java"))
		.pause(11)
	}

	object ItBooksDetails {
		val itBooksDetails = exec(http("ItBooksDetails")
			.get("/itBooks/details/9780321812186"))
		.pause(7)
	}
	val scn1 = scenario("Search IT Books").exec(Home.home,
												Login.login,
												SearchItBookForm.searchItBookForm,
												SearchItBooks.searchItBooks)
	
	val scn2 = scenario("IT Books details").exec(Home.home,
												Login.login,
												SearchItBookForm.searchItBookForm,
												SearchItBooks.searchItBooks,
												ItBooksDetails.itBooksDetails)

	setUp(scn1.inject(rampUsers(5000) during (10 seconds)), scn2.inject(rampUsers(5000) during (10 seconds))).protocols(httpProtocol)
}