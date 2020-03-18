package org.springframework.samples.petclinic.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))

public class ReadBookServiceTest {

	@Autowired
	private ReadBookService sut;
	
	@Test
	void shoulReadBook() {
		int bookId = 1; 
		String username = "owner1";
		Boolean ownerReadBook = this.sut.esReadBook(bookId, username);
		Assertions.assertThat(ownerReadBook).isTrue();
	}
	
	@Test
	void shoulNotReadBook() {
		int bookId = 2; 
		String username = "admin1";
		Boolean adminReadBook = this.sut.esReadBook(bookId, username);
		Assertions.assertThat(adminReadBook).isFalse();
	}
	
	@Test
	void shouldDeleteReadBooksByBookId() {
		int bookId = 1;
		int readBookId = 4;
		this.sut.deleteReadBookByBookId(bookId);
		Boolean exitsReadBook = this.sut.existsReadBook(readBookId);
		Assertions.assertThat(exitsReadBook).isFalse();
	}
	
}
