
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.BookInNew;
import org.springframework.samples.petclinic.model.New;
import org.springframework.samples.petclinic.service.exceptions.CantDeleteBookInNewException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class NewServiceTest {

	@Autowired
	private NewService			sut;

	@Autowired
	private BookInNewService	bookInNewService;


	@Test
	void shouldGetNewsIdsFromBookId() {
		int bookId = 2;
		List<Integer> newsIds = this.sut.getNewsFromBook(bookId);
		Assertions.assertThat(newsIds.size()).isEqualTo(1);
		Assertions.assertThat(newsIds).contains(1);

		bookId = 7;
		newsIds = this.sut.getNewsFromBook(bookId);
		Assertions.assertThat(newsIds).isEmpty();
	}

	@Test
	void shouldNotDeleteNewWithMoreThanOneBook() { //Como hay mas de un libro deberia borrar el libro de la noticia, pero permanecer la noticia
		int newId = 1;
		int bookId = 2;

		this.sut.deleteNew(newId, bookId);

		Boolean existsNew = this.sut.existsNewById(newId);
		Assertions.assertThat(existsNew).isTrue();

		List<Integer> bookIds = this.bookInNewService.getBooksInNewFromNew(newId);
		Assertions.assertThat(bookIds.size()).isEqualTo(1);
	}

	@Test
	void shouldDeleteNewWithOneBook() { //Como solo hay un libro deberia borrar el libro de la noticia y la propia noticia
		int newId = 2;
		int bookId = 11;

		this.sut.deleteNew(newId, bookId);

		Boolean existsNew = this.sut.existsNewById(newId);
		Assertions.assertThat(existsNew).isFalse();
		List<Integer> bookIds = this.bookInNewService.getBooksInNewFromNew(newId);
		Assertions.assertThat(bookIds).isEmpty();
	}

	@Test
	void shouldGetAllNewOrderByDate() {

		List<New> news = (List<New>) this.sut.getAllNews();

		Assertions.assertThat(news.size() == 6);
		Assertions.assertThat(news.get(0).getId() == 5);
	}

	@Test
	void shouldGetBooksFromNewsId() {
		List<Book> books = (List<Book>) this.sut.getBooksFromNews(1);
		Assertions.assertThat(books.size() == 2);
		Assertions.assertThat(books.get(0).getId() == 2);
		Assertions.assertThat(books.get(1).getId() == 3);
	}

	@Test
	void shouldDeteteNewById() {
		this.sut.deleteById(1);
		Assertions.assertThat(this.sut.getNewById(1)).isNull();
	}

	@Test
	void shouldNotAddDuplicatedBookInNew() {
		int newId = 1;
		int bookId1 = 4;
		int bookId2 = 5;
		this.sut.saveBookInNew(newId, bookId1);
		this.sut.saveBookInNew(newId, bookId1);
		this.sut.saveBookInNew(newId, bookId2);
		this.sut.saveBookInNew(newId, bookId2);
		BookInNew book = this.bookInNewService.getByNewIdBookId(newId, bookId1);
		Assertions.assertThat(book).isNotNull();

	}

	@Test
	void shouldDeleteBookInNew() throws DataAccessException, CantDeleteBookInNewException {
		int newId = 6;
		int bookId = 3;

		this.sut.deleteBookInNew(newId, bookId);

	}

	@Test
	void shouldNotDeleteBookInNew() {
		int newId = 2;
		int bookId = 11;

		try {
			this.sut.deleteBookInNew(newId, bookId);

		} catch (CantDeleteBookInNewException e) {
			Assertions.assertThat(e.getCause());
		}
	}

	@Test
	public void shouldSaveNew() {
		New neew = new New();
		neew.setBody("body");
		LocalDate fecha = LocalDate.of(2019, 12, 02);
		neew.setFecha(fecha);
		neew.setHead("head");
		neew.setName("name");
		neew.setRedactor("redactor");
		this.sut.save(neew);
		New neew2 = this.sut.getNewById(neew.getId());
		Assertions.assertThat(neew2).isNotNull();
		Assertions.assertThat(neew2.getName()).isEqualTo("name");

	}
}
