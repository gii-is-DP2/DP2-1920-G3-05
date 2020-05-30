
package org.springframework.samples.petclinic.api;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import io.restassured.RestAssured;
import lombok.extern.java.Log;

@Log
class QuoteApiTests {

	@LocalServerPort
	private int port;


	@Test
	void testgetQuoteFromApi() {
		RestAssured.when().get("https://api.quotable.io/random").then().statusCode(200).assertThat().body("content", Matchers.notNullValue()).and().body("author", Matchers.notNullValue());
	}
}
