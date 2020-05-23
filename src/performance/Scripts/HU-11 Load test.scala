package searchMeeting

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

	object MeetingSearchForm {
		val meetingSearchForm = exec(http("MeetingsSearchForm")
			.get("/meetings/find"))
		.pause(14)
	}

	object SearchMeetingSeveralResults {
		val searchMeetingSeveralResults = exec(http("SearchMeetingSeveralResults")
			.get("/meetings?name=el")
			.check(currentLocation.is("http://www.dp2.com/meetings?name=el")))
		.pause(10)
	}

	object SearchMeetingsOneResult {
		val searchMeetingsOneResult = exec(http("SearchMeetingsOneResult")
			.get("/meetings?name=la+musica")
			.check(currentLocation.is("http://www.dp2.com/meetings/9")))
		.pause(12)
	}

	val scn1 = scenario("Search meetings several result").exec(Home.home,
																Login.login,
																MeetingSearchForm.meetingSearchForm,
																SearchMeetingSeveralResults.searchMeetingSeveralResults)

	val scn2 = scenario("Search meetings one result").exec(Home.home,
															Login.login,
															MeetingSearchForm.meetingSearchForm,
															SearchMeetingsOneResult.searchMeetingsOneResult)

	setUp(scn1.inject(rampUsers(3500) during (100 seconds)), 
		scn2.inject(rampUsers(3500) during (100 seconds)))
		.protocols(httpProtocol)
		.assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}