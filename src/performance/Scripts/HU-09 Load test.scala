
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU09LoadTest extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.png""", """.*.pneg""", """.*.jpg""", """.*.ico""", """.*.jpeg"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_2 = Map("Origin" -> "http://www.dp2.com")

	object Home {
		val home = exec(http("Home")
			.get("/"))
		.pause(9)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(16)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}"))
		.pause(11)
	}

	object FindMeeting {
		val findMeeting = exec(http("FindMeeting")
			.get("/meetings/find"))
		.pause(13)
	}

	object MeetingList {
		val meetingList = exec(http("MeetingList")
			.get("/meetings?name="))
		.pause(18)
	}

	object ShowMeeting {
		val showMeeting = exec(http("ShowMeeting")
			.get("/meetings/14"))
		.pause(10)
	}

	object InscribeSuccesfullMeeting {
		val inscribeSuccesfullMeeting = exec(http("InscribeSuccessfully")
			.get("/meetings/14/inscribe"))
		.pause(20)
	}
	
	object InscribeUnsuccesfullMeeting {
		val inscribeUnsuccesfullMeeting = exec(http("InscribeUnsuccessfully")
			.get("/meetings/6"))
		.pause(16)
	}

	val InscribeGoodMeetingScenario = scenario("InscribeMeetingSuccesfully").exec(Home.home,
											Login.login,
											FindMeeting.findMeeting,
											MeetingList.meetingList,
											ShowMeeting.showMeeting,
											InscribeSuccesfullMeeting.inscribeSuccesfullMeeting)

	val InscribeBadMeetingScenario = scenario("InscribeMeetingUnsuccesfully").exec(Home.home,
											Login.login,
											FindMeeting.findMeeting,
											MeetingList.meetingList,
											InscribeUnsuccesfullMeeting.inscribeUnsuccesfullMeeting) 

	setUp(InscribeGoodMeetingScenario.inject(rampUsers(1300) during (100 seconds)),
			InscribeBadMeetingScenario.inject(rampUsers(1300) during (100 seconds)))
		.protocols(httpProtocol)
		.assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )



}