package mangeReviews

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class BothScenarios extends Simulation {

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

	object ReadBooksList {
		val readBooksList = exec(http("ReadBooksList")
			.get("/books/readBooks"))
		.pause(6)
	}

	object ShowBook {
		val showBook = exec(http("ShowBook")
			.get("/books/1"))
		.pause(7)
	}

	object ShowBookWriteReview {
		val showBookWriteReview = exec(http("ShowBookToWriteReview")
			.get("/books/7"))
		.pause(7)
	}

	object ReviewsList {
		val reviewsList = exec(http("ReviewsList")
			.get("/books/1/reviews"))
		.pause(8)
	}

	object ReviewShow {
		val reviewShow = exec(http("ReviewShow")
			.get("/reviews/2"))
		.pause(8)
	}

	object ReviewEditForm {
		val reviewEditForm = exec(http("ReviewEditForm")
			.get("/books/1/reviews/2/edit")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(20)
		.exec(http("UpdatedReview")
			.post("/books/1/reviews/2/edit")
			.headers(headers_2)
			.formParam("reviewId", "2")
			.formParam("bookId", "1")
			.formParam("title", "Editing review")
			.formParam("raiting", "5")
			.formParam("opinion", "Excellent")
			.formParam("_csrf", "${stoken}"))
		.pause(7)
	}

	object WriteReviewForm {
		val writeReviewForm = exec(http("WriteReviewForm")
			.get("/books/7/reviews/new")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))		
			.pause(22)
			.exec(http("WritedReview")
			.post("/books/7/reviews/new")
			.headers(headers_2)
			.formParam("bookId", "7")
			.formParam("title", "Review")
			.formParam("raiting", "4")
			.formParam("opinion", "Very good")
			.formParam("_csrf", "${stoken}"))
		.pause(17)
	}

	val EditReviewScenario = scenario("EditReview").exec(Home.home,
											Login.login,
											ReadBooksList.readBooksList,
											ShowBook.showBook,
											ReviewsList.reviewsList,
											ReviewShow.reviewShow,
											ReviewEditForm.reviewEditForm)

	val WriteReviewScenario = scenario("WriteReview").exec(Home.home,
											Login.login,
											ReadBooksList.readBooksList,
											ShowBookWriteReview.showBookWriteReview,
											WriteReviewForm.writeReviewForm) 

	setUp(EditReviewScenario.inject(rampUsers(15000) during (10 seconds)),
			WriteReviewScenario.inject(rampUsers(15000) during (10 seconds))).protocols(httpProtocol)
}