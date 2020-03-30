package org.springframework.samples.petclinic.service;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class BookInNewServiceTests {

	@Autowired
	private BookInNewService sut;
	
	@Test
	void shouldGetBooksInNew() {
		int newId = 1;
		List<Integer> booksIds = this.sut.getBooksInNewFromNew(newId);
		Assertions.assertThat(booksIds.size()).isEqualTo(2);
		Assertions.assertThat(booksIds).contains(2,3);

		newId = 2;
		booksIds = this.sut.getBooksInNewFromNew(newId);
		Assertions.assertThat(booksIds.size()).isEqualTo(1);
		Assertions.assertThat(booksIds).contains(11);
	}
	
	@Test
	void shouldDeleteBookFromNew() {
		int newId = 1;
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
