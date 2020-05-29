package org.springframework.samples.petclinic.api;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import lombok.extern.java.Log;
@Log
 class ITApiTests {
	@Test
	 void testGetBookDetails() {
		when()
		.get("https://api.itbook.store/1.0/books/9781617290459").then().statusCode(200).and()
		.assertThat()
		.body("title", equalTo("Java Persistence with Hibernate, 2nd Edition"))
		.body("authors", equalTo("Christian Bauer, Gavin King, Gary Gregory"))
		.body("publisher", equalTo("Manning"))
		.body("pages", equalTo("608"))
		.body("rating", equalTo("4"))
		.body("isbn10", equalTo("1617290459"));
	}
	@Test
	 void testFindBook() {
		when()
		.get("https://api.itbook.store/1.0/search/King").then().statusCode(200).and()
		.assertThat()
		.body("total", equalTo("18"));
	}
}
