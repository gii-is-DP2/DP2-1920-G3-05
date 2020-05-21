import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class PublicationStressTest extends Simulation {

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

	object ShowBookWritePublication {
		val showBookWritePublication = exec(http("ShowBookToWritePublication")
			.get("/books/7"))
		.pause(7)
	}

	object PublicationsList {
		val publicationsList = exec(http("PublicationsList")
			.get("/books/1/publications"))
		.pause(8)
	}

	object PublicationShow {
		val publicationShow = exec(http("PublicationShow")
			.get("/publications/1"))
		.pause(8)
	}

	object PublicationEditFormSuccessfully {
		val publicationEditFormSuccessfully = exec(http("PublicationEditForm")
			.get("/publications/1/updateForm")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(20)
		.exec(http("UpdatedPublicationGoodData")
			.post("/publications/update/1")
			.headers(headers_2)
			.formParam("title", "publication 1")
			.formParam("image", "https://imagessl3.casadellibro.com/a/l/t5/93/9788497593793.jpg")
			.formParam("description", "this is tests data")
			.formParam("id", "1")
			.formParam("_csrf", "${stoken}"))
		.pause(7)
	}

	object PublicationEditFormUnsuccessfully {
		val publicationEditFormUnsuccessfully = exec(http("PublicationEditForm")
			.get("/publications/1/updateForm")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(20)
		.exec(http("UpdatedPublicationBadData")
			.post("/publications/update/1")
			.headers(headers_2)
			.formParam("title", "publication 1")
			.formParam("image", "imagessl3.casadellibro.com/a/l/t5/93/9788497593793.jpg")
			.formParam("description", "this is tests data")
			.formParam("id", "1")
			.formParam("_csrf", "${stoken}"))
		.pause(7)
	}

	val EditPublicationScenario = scenario("EditPublicationSuccesfully").exec(Home.home,
											Login.login,
											ReadBooksList.readBooksList,
											ShowBook.showBook,
											PublicationsList.publicationsList,
											PublicationShow.publicationShow,
											PublicationEditFormSuccessfully.publicationEditFormSuccessfully)

	val WritePublicationScenario = scenario("EditPublicationunsuccesfully").exec(Home.home,
											Login.login,
											ReadBooksList.readBooksList,
											ShowBookWritePublication.showBookWritePublication,
											PublicationEditFormUnsuccessfully.publicationEditFormUnsuccessfully) 

	setUp(EditPublicationScenario.inject(rampUsers(5000) during (10 seconds)),
			WritePublicationScenario.inject(rampUsers(5000) during (10 seconds))).protocols(httpProtocol)
		
     


		
}