package org.springframework.samples.petclinic.api;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import lombok.extern.java.Log;
@Log
public class ITApiTests {
	@Test
	public void testGetBookDetails() {
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
	public void testFindBook() {
		when()
		.get("https://api.itbook.store/1.0/search/King").then().statusCode(200).and()
		.assertThat()
		.body("total", equalTo("18"));
	}
}
