package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU13LoadTest extends Simulation {
	val sessionHeaders = Map("Authorization" -> "Bearer ${_csrf}",
                           "Content-Type" -> "application/json")
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

	val csvFeeder = csv("AddBookISBNStress.csv").circular
	val csvFeeder2 = csv("Ids.csv").circular
	val csvFeeder3 = csv("Ids2.csv").circular
	val csvFeeder4 = csv("Ids3.csv").circular	
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
	object AddBooksForm {
		val addBooksForm = exec(http("AddBooksForm")
			.get("/books/add"))
		.pause(8)

	}

	object AddBookSuccessfull {
		val addBookSuccess = exec(http("AddBook")
			.get("/books/add")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))	
		.pause(83)
		.feed(csvFeeder)
		.feed(csvFeeder2)
		.exec(http("AddBookGoodData")
			.post("/books/save")
			.headers(headers_2)
			.formParam("title", "Test")
			.formParam("id", "${id}")
			.formParam("author", "Test Author")
			.formParam("editorial", "DP")
			.formParam("genre", "Adventure")
			.formParam("pages", "456")
			.formParam("synopsis", "This is a test book")
			.formParam("ISBN", "${isbn}")
			.formParam("publicationDate", "2020/05/03")
			.formParam("image", "https://imagessl4.casadellibro.com/a/l/t5/44/9788498387544.jpg")
			.formParam("_csrf", "${stoken}")
			)
		.pause(16)

	}
	object GetBook {
		val getBook = feed(csvFeeder3).exec(http("GetABook")
			.get("/books/${id}")
			.headers(headers_0))
		.pause(13)
	}
	object AddToReadBook {
		val addToReadBook =	feed(csvFeeder4).exec(http("AddedToReadBooks2")
			.get("/books/readBooks/${id}")
			.headers(headers_0))
		.pause(10)
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
	object AddedToReadBooks2{
		val addedToReadBooks = exec(http("AddedToReadBooks2")
			.get("/books/readBooks/2")
			.headers(headers_0))
		.pause(10)
	}
	object GetABook1 {
		val getABook = exec(http("GetABook1")
			.get("/books/1")
			.headers(headers_0))
		.pause(13)
	}
	object AddedToReadBooks1{
		val addedToReadBooks = exec(http("AddedToReadBooks1")
			.get("/books/readBooks/1")
			.headers(headers_0))
		.pause(10)
	}


	val positiveScn = scenario("AddReadBookPositive").exec(Home.home,
													Login.login,
													FindBooks.findBooks,
													AddBooksForm.addBooksForm,
													AddBookSuccessfull.addBookSuccess,
													GetBook.getBook,
													AddToReadBook.addToReadBook
													)
	val negativeScn = scenario("AddReadBookNegative").exec(Home.home,
													Login.login,
													FindBooks.findBooks,
													GetBooks.getBooks,
													GetABook1.getABook,
													AddedToReadBooks1.addedToReadBooks)	


	setUp(
		positiveScn.inject(rampUsers(40) during (100 seconds)),
		negativeScn.inject(rampUsers(40) during (100 seconds))
	).protocols(httpProtocol)
	.assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}