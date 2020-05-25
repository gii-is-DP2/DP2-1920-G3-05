package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU12StressTest extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png""", """.*.jpg""", """.*.jpeg"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	object Home{
		val home = exec(http("home")
			.get("/")
			.headers(headers_0))
		.pause(2)
	}

	object Login{
    val login = exec(http("login")
     		 .get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)
		.check(css("input[name=_csrf]", "value").saveAs("stoken")))
    ).pause(8)
    .exec(
      http("logged")
       .post("/login")
		.headers(headers_3)
        .formParam("username", "reader1")
        .formParam("password", "reader")        
        .formParam("_csrf", "${stoken}")
    ).pause(10)
  }

		object FormFindBook{
			val formFindBook = exec(http("formFindBook")
			.get("/books/find")
			.headers(headers_0))
		.pause(15)
		}

		object ListBook{
			val listBook = exec(http("listBook")
			.get("/books?title=Harry+Potter")
			.headers(headers_0))
		.pause(9)
		}

		object ShowBook{
			val showBook = exec(http("showBook")
			.get("/books?title=9788466345347")
			.headers(headers_0))
		.pause(9)
		}

	val filterBookList = scenario("FilterBookList").exec(Home.home,Login.login,
	FormFindBook.formFindBook,ListBook.listBook)

	val filterBookShow = scenario("FilterBookShow").exec(Home.home,Login.login,
	FormFindBook.formFindBook,ShowBook.showBook)

	setUp(filterBookList.inject(rampUsers(7000) during(10 seconds)),filterBookShow.inject(rampUsers(7000) during(10 seconds)))
	.protocols(httpProtocol)
}