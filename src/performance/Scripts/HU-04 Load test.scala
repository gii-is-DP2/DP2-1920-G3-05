package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU04LoadTest extends Simulation {

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

	object FindBooksForm {
		val findBooksForm = exec(http("FindBooksForm")
			.get("/books/find"))
		.pause(31)
	}
	object FindBooksByTitle {
		val findbooksByTitle = exec(http("FindBooksByTitle")
			.get("/books?title="))
		.pause(15)
	}
	object VerifyBook {
		val verifyBook = exec(http("VerifyBook")
			.get("/books/3"))
		.pause(20)
	}
	object VerifiedBook {
		val verifiedBook = exec(http("VerifiedBook")
			.get("/admin/books/3/verify"))
		.pause(25)
	}
	object VerifyVerifiedBook {
		val verifyVerifiedBook = exec(http("VerifyVerifiedBook")
			.get("/books/1"))
		.pause(23)
	}
	
	val positiveScn = scenario("Verify non-verified book").exec(Home.home,Login.login,
	FindBooksForm.findBooksForm,FindBooksByTitle.findbooksByTitle,VerifyBook.verifyBook,VerifiedBook.verifiedBook)
		
	val negativeScn = scenario("Verify verified book").exec(Home.home,Login.login,
	FindBooksForm.findBooksForm,FindBooksByTitle.findbooksByTitle,VerifyVerifiedBook.verifyVerifiedBook)

	setUp(positiveScn.inject(rampUsers(35) during (100 seconds)),negativeScn.inject(rampUsers(35) during (100 seconds)))
	.protocols(httpProtocol).assertions(
       	 global.responseTime.max.lt(5000),    
       	 global.responseTime.mean.lt(1000),
       	 global.successfulRequests.percent.gt(95))
}