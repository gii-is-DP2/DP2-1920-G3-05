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

