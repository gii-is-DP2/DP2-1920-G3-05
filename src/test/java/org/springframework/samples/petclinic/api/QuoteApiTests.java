package org.springframework.samples.petclinic.api;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;

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
