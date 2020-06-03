package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU21LoadTest extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png""", """.*.jpg""",""".*.jpeg"""), WhiteList())
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map(
		"Accept" -> "*/*",
		"Accept-Encoding" -> "identity",
		"Proxy-Connection" -> "Keep-Alive",
		"User-Agent" -> "Microsoft BITS/7.8")

	val headers_2 = Map(
		"Accept" -> "*/*",
		"Accept-Encoding" -> "identity",
		"Proxy-Connection" -> "Keep-Alive",
		"Range" -> "bytes=0-1119",
		"User-Agent" -> "Microsoft BITS/7.8")

	val headers_3 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val userData = csv("createUserLoad.csv").queue

    	object Home{
		val home = exec(http("home")
			.get("/")
			.headers(headers_0))
		.pause(2)
	}
	
	object NewUser{
	val newUser = exec(
		http("formNewUser")
		.get("/users/new")
		.headers(headers_0)
		.check(css("input[name=_csrf]", "value").saveAs("userToken")))
		.pause(4)
		.feed(userData)
		.exec(http("createUser")
		.post("/users/new")
		.headers(headers_3)
			.formParam("firstName", "${firstName}")
			.formParam("lastName", "${lastName}")
			.formParam("address", "${address}")
			.formParam("city", "${city}")
			.formParam("telephone", "${telephone}")
			.formParam("user.username", "${username}")
			.formParam("user.password", "${password}")
			.formParam("_csrf", "${userToken}"))
		.pause(15)
	}

	object Login{
	val login = exec(http("login")
     		 .get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_1)
		.check(css("input[name=_csrf]", "value").saveAs("stoken")))
    		).pause(8)
		.exec(http("logged").post("/login")
		.headers(headers_2)
        	  .formParam("username", "reader1")
        	  .formParam("password", "reader")        
      		  .formParam("_csrf", "${stoken}")
    		).pause(8)
	}

	object UpdateUser{
	val updateUser = exec(http("formUpdateUser")
			.get("/users/update")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("updateToken")))
		.pause(13)
		.exec(http("updateUser")
			.post("/users/update")
			.headers(headers_3)
			.formParam("firstName", "Test firstname")
			.formParam("lastName", "Test lastname")
			.formParam("address", "Test address")
			.formParam("city", "Test city")
			.formParam("telephone", "123456789")
			.formParam("user.password", "reader")
			.formParam("_csrf", "${updateToken}"))
		.pause(9)
	}

	val createUser = scenario("CreateUser").exec(Home.home, NewUser.newUser)

	val updateUser = scenario("UpdateUser").exec(Home.home, Login.login, UpdateUser.updateUser)

	setUp(createUser.inject(rampUsers(3300) during(100 seconds)),updateUser.inject(rampUsers(3300) during(100 seconds)))
	.protocols(httpProtocol)
	.assertions(global.responseTime.max.lt(5000),
				global.responseTime.mean.lt(1000),
				global.successfulRequests.percent.gt(95))
}