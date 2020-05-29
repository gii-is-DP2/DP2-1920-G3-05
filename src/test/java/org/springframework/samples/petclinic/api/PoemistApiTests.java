package org.springframework.samples.petclinic.api;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;

import lombok.extern.java.Log;


@Log
public class PoemistApiTests {
	@LocalServerPort
	private int port;
	@Test
	public void testgetPoemFromApi() {
		when()
		.get("https://www.poemist.com/api/v1/randompoems")
		.then().statusCode(200)
		.assertThat()
		.body("title", notNullValue())
		.and()
		.body("content", notNullValue())
		.and()
		.body("poet", notNullValue());
	}
}

