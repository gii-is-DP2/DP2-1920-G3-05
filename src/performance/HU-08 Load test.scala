package createMeeting

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

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_4 = Map(
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Origin" -> "http://www.dp2.com",
		"Upgrade-Insecure-Requests" -> "1")

	object Home {
		val home = exec(http("HOME")
			.get("/"))
		.pause(6)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(20)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_4)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}"))
		.pause(12)
	}

	object FindBooksForm {
		val findBooksForm = exec(http("FindBooksForm")
			.get("/books/find")
			.headers(headers_0))
		.pause(9)
	}

	object BooksList {
		val booksList = exec(http("BooksList")
			.get("/books?title=")
			.headers(headers_0))
		.pause(6)
	}

	object BookShow {
		val bookShow = exec(http("BookShow")
			.get("/books/1")
			.headers(headers_0))
		.pause(13)
	}

	object CreateMeetingSuccessfully {
		val createMeetingSuccessfully = exec(http("CreateMeetingForm")
			.get("/admin/books/1/meetings/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(26)
		.exec(http("CreatedMeetingSuccessfully")
			.post("/admin/books/1/meetings/new")
			.headers(headers_3)
			.formParam("bookId", "1")
			.formParam("name", "Meeting 1")
			.formParam("place", "Seville")
			.formParam("start", "2100-10-10T10:00")
			.formParam("end", "2100-10-10T12:00")
			.formParam("capacity", "30")
			.formParam("_csrf", "${stoken}"))
		.pause(16)
	}

	object CreateMeetingLessThanOneHourDuration {
		val createMeetingLessThanOneHourDuration = exec(http("CreateMeetingForm")
			.get("/admin/books/1/meetings/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
			.pause(26)
			.exec(http("CreateMeetingLessThanOneHourDuration")
			.post("/admin/books/1/meetings/new")
			.headers(headers_3)
			.formParam("bookId", "1")
			.formParam("name", "Meeting2")
			.formParam("place", "Seville")
			.formParam("start", "2100-10-10T10:00")
			.formParam("end", "2100-10-10T10:30")
			.formParam("capacity", "25")
			.formParam("_csrf", "${stoken}"))
		.pause(19)
	}

	val scn1 = scenario("Create Meeting Successfully").exec(Home.home,
												Login.login,
												FindBooksForm.findBooksForm,
												BooksList.booksList,
												BookShow.bookShow,
												CreateMeetingSuccessfully.createMeetingSuccessfully)

	val scn2 = scenario("Create Meeting that lasts less than 1 hour").exec(Home.home,
												Login.login,
												FindBooksForm.findBooksForm,
												BooksList.booksList,
												BookShow.bookShow,
												CreateMeetingLessThanOneHourDuration.createMeetingLessThanOneHourDuration)

	setUp(scn1.inject(rampUsers(90) during (100 seconds)),
		  scn2.inject(rampUsers(90) during (100 seconds)))
		.protocols(httpProtocol)
		.assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(92)
     )
}