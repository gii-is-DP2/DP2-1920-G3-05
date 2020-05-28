package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import scala.util.Random

class HU10StressTest extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png""", """.*.jpg""", """.*.jpeg"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36")

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

	object Home {
		val home = 	exec(http("Home")
			.get("/news")
			.headers(headers_0))
		.pause(7)
	}
	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2))
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(8)
		.exec(http("LoggedAsAdmin")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}"))
		.pause(7)
	}
	object SearchMeetings {
		val searchMeetings = exec(http("SearchMeetings")
			.get("/meetings/find")
			.headers(headers_0))
		.pause(17)
	}
	object MeetingsFounds {
		val meetingsFounds = exec(http("MeetingsFounds")
			.get("/meetings?name=")
			.headers(headers_0))
		.pause(12)
	}
	object GetMeeting14 {
		val getMeeting = exec(http("GetMeeting1")
			.get("/meetings/14")
			.headers(headers_0))
		.pause(33)
	}
	object InscribeSuccesfullMeeting {
		val inscribeSuccesfullMeeting = exec(http("InscribeSuccessfully")
			.get("/meetings/14/inscribe"))
		.pause(20)
	}
	object GetMeeting4 {
		val getMeeting = exec(http("GetMeeting4")
			.get("/meetings/4")
			.headers(headers_0))
		.pause(33)
	}
	object UnsubscribeFromMeeting14 {
		val unsubscribeFromMeeting = exec(http("UnsubscribeFromMeeting1")
			.get("/meetings/14/unsuscribe")
			.headers(headers_0))
		.pause(17)
	}
	object UnsubscribeFromMeeting4 {
		val unsubscribeFromMeeting = exec(http("UnsubscribeFromMeeting4")
			.get("/meetings/4/unsuscribe")
			.headers(headers_0))
		.pause(17)
	}
		object InscribeUnsuccesfullMeeting {
		val inscribeUnsuccesfullMeeting = exec(http("InscribeUnsuccessfully")
			.get("/meetings/6/inscribe")
			.check(status.is(404)))
		.pause(16)
	}
		object ShowMeeting6 {
		val showMeeting6 = exec(http("ShowMeeting")
			.get("/meetings/6"))
		.pause(10)
	}

	val feeder = Iterator.continually(Map("username" -> (Random.alphanumeric.take(20).mkString )))

	object NewUser{
		val newUser = exec(
		http("formNewUser")
		.get("/users/new")
		.headers(headers_0)
		.check(css("input[name=_csrf]", "value").saveAs("userToken")))
		.pause(4)
		.feed(feeder)
		.exec(http("createUser")
		.post("/users/new")
		.headers(headers_3)
			.formParam("firstName", "admin1")
			.formParam("lastName", "admin1")
			.formParam("address", "adrres")
			.formParam("city", "sevilla")
			.formParam("telephone", "651665914")
			.formParam("user.username", "${username}")
			.formParam("user.password", "1234")
			.formParam("_csrf", "${userToken}"))
		.pause(15)
		.exec(http("LoginNewUser")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2))
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(8)
		.exec(http("LoggedAsAdmin")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "${username}")
			.formParam("password", "1234")
			.formParam("_csrf", "${stoken}"))
		.pause(7)
	}



	object AddReadBook {
		val addReadBook = exec(http("addReadBook")
			.get("/books/readBooks/1"))
		.pause(10)
	}

	val positiveScn = scenario("UnsubscribeMeetingPositive").exec(Home.home,
													NewUser.newUser,
													AddReadBook.addReadBook,
													SearchMeetings.searchMeetings,
													MeetingsFounds.meetingsFounds,
													GetMeeting14.getMeeting,
													InscribeSuccesfullMeeting.inscribeSuccesfullMeeting,
													UnsubscribeFromMeeting14.unsubscribeFromMeeting)

	val negativeScnUnsubscribe = scenario("UnsubscribeMeetingNegative").exec(Home.home,
													Login.login,
													SearchMeetings.searchMeetings,
													MeetingsFounds.meetingsFounds,
													GetMeeting4.getMeeting,
													UnsubscribeFromMeeting4.unsubscribeFromMeeting)	
	val inscribeBadMeetingScenario = scenario("InscribeMeetingUnsuccesfully").exec(Home.home,
											Login.login,
											SearchMeetings.searchMeetings,
											MeetingsFounds.meetingsFounds,
											ShowMeeting6.showMeeting6,
											InscribeUnsuccesfullMeeting.inscribeUnsuccesfullMeeting) 
		
		

	setUp(
		positiveScn.inject(rampUsers(55000) during (10 seconds)),
		negativeScnUnsubscribe.inject(rampUsers(55000) during (10 seconds)),
		inscribeBadMeetingScenario.inject(rampUsers(55000) during (10 seconds))
	).protocols(httpProtocol)
}