package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU20LoadTest extends Simulation {

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
		.pause(9)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(13)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}"))
		.pause(7)
	}

	object ListUsers {
		val listUsers = exec(http("ListUsers")
			.get("/admin/listUsers"))
		.pause(31)
	}
	object VerifyUser {
		val verifyUser = exec(http("VerifyUser")
			.get("/admin/users/2/verify"))
		.pause(15)
	}
	object VerifyVerifiedUser {
		val verifyVerifiedUser = exec(http("VerifyVerifiedUser")
			.get("/admin/listUsers"))
		.pause(20)
	}
	
	
	val positiveScn = scenario("Verify non-verified user").exec(Home.home,Login.login,
	ListUsers.listUsers,VerifyUser.verifyUser)
		
	val negativeScn = scenario("Verify verified user").exec(Home.home,Login.login,
	ListUsers.listUsers,VerifyVerifiedUser.verifyVerifiedUser)

	setUp(positiveScn.inject(rampUsers(5500) during (100 seconds)),negativeScn.inject(rampUsers(5500) during (100 seconds)))
	.protocols(httpProtocol).assertions(
       	 global.responseTime.max.lt(5000),    
       	 global.responseTime.mean.lt(1000),
       	 global.successfulRequests.percent.gt(95))
}