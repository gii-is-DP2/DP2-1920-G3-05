package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU05LoadTest extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png""", """.*.jpg""", """.*.jpeg"""), WhiteList())
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "*/*",
		"Accept-Encoding" -> "identity",
		"Proxy-Connection" -> "Keep-Alive",
		"User-Agent" -> "Microsoft BITS/7.8")

	val headers_3 = Map(
		"Accept" -> "*/*",
		"Accept-Encoding" -> "identity",
		"Proxy-Connection" -> "Keep-Alive",
		"Range" -> "bytes=0-1119",
		"User-Agent" -> "Microsoft BITS/7.8")
		
	val headers_8 = Map(
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
        .formParam("username", "admin1")
        .formParam("password", "4dm1n")        
        .formParam("_csrf", "${stoken}")
    ).pause(10)
  }   
  
   	object CreateNew{
        val createNew = exec(http("formCreateNew")
			.get("/admin/news/create")
			.headers(headers_0)
            .check(css("input[name=_csrf]", "value").saveAs("tokenCreateNew")))
		.pause(12)
		.exec(http("createNew")
			.post("/admin/news/create")
			.headers(headers_8)
			.formParam("head", "Test")
			.formParam("name", "Test new")
			.formParam("body", "Test body")
			.formParam("redactor", "Test redactor")
			.formParam("tags", "#TestTags")
			.formParam("img", "https://images-eu.ssl-images-amazon.com/images/I/51QsPyrJdYL.jpg")
			.formParam("fecha", "2020/04/21")
			.formParam("_csrf", "${tokenCreateNew}")
			.check(currentLocation.transform(s => {
   			 val pattern = """.*/books/([0-9]+)""".r
    		val pattern(temp) = s
   			 temp
  			}).saveAs("idNew"))
		)
		.pause(3)
		.exec(http("addBook")
			.get("/admin/news/books/add/${idNew}/3")
			.headers(headers_0)
			.resources(http("request_26")
			.get("/admin/news/books/add/${idNew}/3")
			.headers(headers_0)))
		.pause(5)
		.exec(http("saveNew")
			.get("/admin/news/books/save/${idNew}")
			.headers(headers_0))
		.pause(10)
  }   

	object UpdateNew{
        val updateNew = exec(http("showNew")
			.get("/admin/news/5")
			.headers(headers_0)
            .check(css("input[name=_csrf]", "value").saveAs("tokenUpdateNew")))
		    .pause(29)
		    .exec(http("updateNew")
			.post("/admin/news/5")
			.headers(headers_8)
			.formParam("head", "Update test")
			.formParam("name", "Update test new")
			.formParam("body", "Update test body")
			.formParam("redactor", "Update test redactor")
			.formParam("tags", "#UpdateTestNew")
			.formParam("img", "https://infoliteraria.com/wp-content/uploads/2020/02/El-fin-y-otros-inicios.jpg")
			.formParam("fecha", "2020/02/17")
			.formParam("_csrf", "${tokenUpdateNew}"))
		.pause(10)
    }
		
  val createNew = scenario("CreateNew").exec(Home.home,Login.login,CreateNew.createNew)

	val updateNew = scenario("UpdateNew").exec(Home.home,Login.login,UpdateNew.updateNew)

	setUp(createNew.inject(rampUsers(1100) during(100 seconds)),updateNew.inject(rampUsers(1100) during(100 seconds)))
	.protocols(httpProtocol)
	.assertions(global.responseTime.max.lt(5000),
				global.responseTime.mean.lt(1000),
				global.successfulRequests.percent.gt(95))
}