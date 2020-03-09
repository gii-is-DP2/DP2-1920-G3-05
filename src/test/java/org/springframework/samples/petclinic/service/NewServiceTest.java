
package org.springframework.samples.petclinic.service;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class NewServiceTest {

	@Autowired
	private NewService sut;
	
	@Autowired
	private BookInNewService bookInNewService;


	@Test
	void shouldGetNewsIdsFromBookId() {
		int bookId = 2;
		List<Integer> newsIds = this.sut.getNewsFromBook(bookId);
		Assertions.assertThat(newsIds.size()).isEqualTo(1);
		Assertions.assertThat(newsIds).contains(1);

		
		bookId = 1;
		newsIds = this.sut.getNewsFromBook(bookId);
		Assertions.assertThat(newsIds).isEmpty();
	}
	
	@Test
	void shouldDeleteNewWithMoreThanOneBook() { //Como hay mas de un libro deberia borrar el libro de la noticia, pero permanecer la noticia
		int newId = 1;
		int bookId = 2;
		Boolean existsNew = this.sut.existsNewById(newId);
		Assertions.assertThat(existsNew).isTrue();
		List<Integer> bookIds = this.bookInNewService.getBooksInNewFromNew(newId);
		Assertions.assertThat(bookIds.size()).isEqualTo(2);
		Assertions.assertThat(bookIds).contains(bookId);
		
		this.sut.deleteNew(newId, bookId);
		
		existsNew = this.sut.existsNewById(newId);
		Assertions.assertThat(existsNew).isTrue();

		bookIds = this.bookInNewService.getBooksInNewFromNew(newId);
		Assertions.assertThat(bookIds.size()).isEqualTo(1);
	}

	@Test
	void shouldDeleteNewWithOneBook() { //Como solo hay un libro deberia borrar el libro de la noticia y la propia noticia
		int newId = 2;
		int bookId = 11;
		Boolean existsNew = this.sut.existsNewById(newId);
		Assertions.assertThat(existsNew).isTrue();
		List<Integer> bookIds = this.bookInNewService.getBooksInNewFromNew(newId);
		Assertions.assertThat(bookIds.size()).isEqualTo(1);
		Assertions.assertThat(bookIds).contains(bookId);
		
		this.sut.deleteNew(newId, bookId);
		
		existsNew = this.sut.existsNewById(newId);
		Assertions.assertThat(existsNew).isFalse();
		bookIds = this.bookInNewService.getBooksInNewFromNew(newId);
		Assertions.assertThat(bookIds).isEmpty();
	}
}
