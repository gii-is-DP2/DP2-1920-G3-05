package org.springframework.samples.petclinic.api;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import lombok.extern.java.Log;

@Log
public class QuoteApiTests {
	@LocalServerPort
	private int port;
	@Test
	public void testgetQuoteFromApi() {
		when()
		.get("https://api.quotable.io/random")
		.then().statusCode(200)
		.assertThat()
		.body("content", notNullValue())
		.and()
		.body("author", notNullValue());
	}
}
