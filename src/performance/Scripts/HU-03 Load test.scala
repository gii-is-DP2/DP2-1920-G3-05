package deleteBook

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class DeleteBookScenarios extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.jpg""", """.*.jpeg""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0")

	val headers_0 = Map(
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map("Origin" -> "http://www.dp2.com")

	val headers_4 = Map(
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Origin" -> "http://www.dp2.com",
		"Upgrade-Insecure-Requests" -> "1")

	val csvFeeder = csv("AddBookISBNFeeder.csv").circular

	val csvDeleteFeeder = csv("DeleteBookFeeder.csv").circular

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(13)
	}

	object LoginAdmin {
		val loginAdmin = exec(http("LoginAdmin")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(20)
		.exec(http("LoggedAdmin")
			.post("/login")
			.headers(headers_4)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}"))
		.pause(12)
	}

	object LoginReader {
		val loginReader = exec(http("LoginReader")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(20)
		.exec(http("LoggedReader")
			.post("/login")
			.headers(headers_4)
			.formParam("username", "reader1")
			.formParam("password", "reader")
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

	object DeleteBookAdmin {
		val deleteBookAdmin = feed(csvDeleteFeeder).exec(http("DeleteBookAdmin")
			.get("/admin/books/delete/${book_id}")
			.headers(headers_0))
		.pause(33)
	}

	object DeleteBookNoAdmin {
		val deleteBookNoAdmin = exec(http("DeleteBookNoAdmin")
			.get("/admin/books/delete/2")
			.check(status.is(403)))
		.pause(8)
	}

	object AddBooksForm {
		val addBooksForm = exec(http("AddBooksForm")
			.get("/books/add"))
		.pause(8)

	}

	object AddBookSuccessfull {
		val addBookSuccess = exec(http("AddBook")
			.get("/books/add")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(10)
		.feed(csvFeeder)
		.exec(http("AddBookGoodData")
			.post("/books/save")
			.headers(headers_2)
			.formParam("title", "Test")
			.formParam("author", "Test Author")
			.formParam("editorial", "DP")
			.formParam("genre", "Adventure")
			.formParam("pages", "456")
			.formParam("synopsis", "This is a test book")
			.formParam("ISBN", "${isbn}")
			.formParam("publicationDate", "2020/05/03")
			.formParam("image", "https://imagessl4.casadellibro.com/a/l/t5/44/9788498387544.jpg")
			.formParam("_csrf", "${stoken}"))
		.pause(16)
	}

	val deleteBookScenarioAdmin = scenario("Admin").exec(Home.home,
											LoginAdmin.loginAdmin,
											FindBooksForm.findBooksForm,
											AddBooksForm.addBooksForm,
											AddBookSuccessfull.addBookSuccess,
											DeleteBookAdmin.deleteBookAdmin)

	val deleteBookScenarioNoAdmin = scenario("NoAdmin").exec(Home.home,
													LoginReader.loginReader,
													FindBooksForm.findBooksForm,
													BooksList.booksList,
													BookShow.bookShow,
													DeleteBookNoAdmin.deleteBookNoAdmin)

    setUp(deleteBookScenarioAdmin.inject(rampUsers(33) during (100 seconds)),
    deleteBookScenarioNoAdmin.inject(rampUsers(33) during (10 seconds)))
    .protocols(httpProtocol)
    .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}