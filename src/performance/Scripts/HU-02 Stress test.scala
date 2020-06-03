package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU02StressTest extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png""", """.*.jpg""", """.*.jpeg"""), WhiteList())
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36")

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

	val headers_4 = Map(
		"A-IM" -> "x-bm,gzip",
		"Proxy-Connection" -> "keep-alive")

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

	object FindBooks {
		val findBooks = exec(http("FindBooks")
			.get("/books/find")
			.headers(headers_0))
		.pause(10)
	}
	object GetBooks {
		val getBooks = exec(http("getBooks")
			.get("/books?title=")
			.headers(headers_0))
		.pause(12)
	}
	object GetABook {
		val getABook = exec(http("GetABook")
			.get("/books/3")
			.headers(headers_0))
		.pause(13)
	}
	val editBookcsv = csv("editBook.csv").circular
	object EditBookWell {
		val editBookWell = exec(http("GetUpdateForm")
			.get("/books/3/updateForm")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(34)
		.feed(editBookcsv)
		.exec(http("editBookWell")
			.post("/books/update/3")
			.headers(headers_3)
			.formParam("title", "${title}")
			.formParam("image", "${image}")
			.formParam("author", "${author}")
			.formParam("genre.name", "Fiction")
			.formParam("id", "${id}")
			.formParam("ISBN", "${ISBN}")
			.formParam("pages", "${pages}")
			.formParam("synopsis", "${synopsis}")
			.formParam("editorial", "${editorial}")
			.formParam("publicationDate", "${publicationDate}")
			.formParam("id", "")
			.formParam("_csrf", "${stoken}")) 
		.pause(10)
	}
	val editBookcsv2 = csv("editBook2.csv").circular
	object EditBookWithErrors {
		val editBookWithErrors = exec(http("GetUpdateForm")
			.get("/books/3/updateForm")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
			.pause(34)
			.feed(editBookcsv2)
			.exec(http("editBookWell")
			.post("/books/update/3")
			.headers(headers_3)
			.formParam("title", "${title}")
			.formParam("image", "${image}")
			.formParam("author", "${author}")
			.formParam("genre.name", "Fiction")
			.formParam("id", "${id}")
			.formParam("ISBN", "${ISBN}")
			.formParam("pages", "-100")
			.formParam("synopsis", "${synopsis}")
			.formParam("editorial", "${editorial}")
			.formParam("publicationDate", "${publicationDate}")
			.formParam("id", "")
			.formParam("_csrf", "${stoken}")) 
		.pause(10)
	}
		

	val positiveScn = scenario("EditBookPositive").exec(Home.home,
													Login.login,
													FindBooks.findBooks,
													GetBooks.getBooks,
													GetABook.getABook,
													EditBookWell.editBookWell)
	val negativeScn = scenario("EditBookNegative").exec(Home.home,
													Login.login,
													FindBooks.findBooks,
													GetBooks.getBooks,
													GetABook.getABook,
													EditBookWithErrors.editBookWithErrors)
	setUp(
		positiveScn.inject(rampUsers(90000) during (10 seconds)),
		negativeScn.inject(rampUsers(90000) during (10 seconds))
	).protocols(httpProtocol)
}