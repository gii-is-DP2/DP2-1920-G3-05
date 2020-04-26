package org.springframework.samples.petclinic.service;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class BookInNewServiceTests {

	@Autowired
	private BookInNewService sut;
	
	@ParameterizedTest
	@CsvSource({
		"1,2",
		"2,1"
	})
	void shouldGetBooksInNew(int newId, int results) {
		List<Integer> booksIds = this.sut.getBooksInNewFromNew(newId);
		Assertions.assertThat(booksIds.size()).isEqualTo(results);
	}
	
	@ParameterizedTest
	@CsvSource({
		"1",
		"4"
	})
	void shouldDeleteBookFromNew(int newId) {
		List<Integer> booksIds = this.sut.getBooksInNewFromNew(newId);
		Assertions.assertThat(booksIds.size()).isEqualTo(2);
		
		this.sut.deleteBookInNew(newId, booksIds.get(0));
		
		booksIds = this.sut.getBooksInNewFromNew(newId);
		Assertions.assertThat(booksIds.size()).isEqualTo(1);
		
		this.sut.deleteBookInNew(newId, booksIds.get(0));
		
		booksIds = this.sut.getBooksInNewFromNew(newId);
		Assertions.assertThat(booksIds).isEmpty();

	}
}
