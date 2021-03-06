package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU14StressTest extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.png""", """.*.pneg""", """.*.jpg""", """.*.ico""", """.*.jpeg""", """.*.JPG"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_0 = Map(
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map("Origin" -> "http://www.dp2.com")

	val csvFeeder = csv("AddBookISBNStress.csv").circular

	object Home {
		val home = exec(http("Home")
			.get("/"))
		.pause(13)
	}

	object Login{
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
		.pause(12)
	}

	object FindBooksForm {
		val findBooksForm = exec(http("FindBooksForm")
			.get("/books/find"))
		.pause(9)
	}

	object FindBooksByTitle {
		val findbooksByTitle = exec(http("FindBooksByTitle")
			.get("/books?title="))
		.pause(15)
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

	val csvDeleteFeeder = csv("DeleteBookFeederStress.csv").circular
	object AddBookToWishList {
		val addBookToWishList = feed(csvDeleteFeeder).exec(http("GetBook")
			.get("/books/${book_id}")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(100)
		.exec(http("AddBookToWishList")
			.post("/books/wishList/${book_id}")
			.headers(headers_2)
			.formParam("_csrf", "${stoken}"))
		.pause(60)
	}
	object AddReadBookToWishList {
		val addReadBookToWishList = exec(http("AddReadBookToWishList")
			.get("/books/10"))
		.pause(25)
		
	}


	val AddGoodBookScenario = scenario("AddBookSuccesfully").exec(Home.home,
											Login.login,
											FindBooksForm.findBooksForm,
											AddBooksForm.addBooksForm,
											AddBookSuccessfull.addBookSuccess,
											AddBookToWishList.addBookToWishList)

	val AddBadBookScenario = scenario("Add read book to wish list").exec(Home.home,Login.login,
	FindBooksForm.findBooksForm,FindBooksByTitle.findbooksByTitle,AddReadBookToWishList.addReadBookToWishList)

	setUp(AddGoodBookScenario.inject(rampUsers(1770) during (10 seconds)),
			AddBadBookScenario.inject(rampUsers(1770) during (10 seconds)))
		.protocols(httpProtocol)
}