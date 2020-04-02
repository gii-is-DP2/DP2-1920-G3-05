
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.BookInNew;
import org.springframework.samples.petclinic.model.New;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.exceptions.CantDeleteBookInNewException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class NewServiceTest {

	@Autowired
	private NewService			sut;

	@Autowired
	private BookInNewService	bookInNewService;
	
	@Autowired
	private UserService 		userService;


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

	@ParameterizedTest
	@CsvSource({
		"0,5","1,4","5,1"
	})
	void shouldGetAllNewOrderByDate(int index, int newId) {

		List<New> news = (List<New>) this.sut.getAllNews();

		Assertions.assertThat(news.get(index).getId() == newId);
	}

	@Test
	void shouldGetNewBookReview() {
		User user = this.userService.findUserByUsername("admin1");
		User user2 = this.userService.findUserByUsername("owner1");
		
		List<New> news = (List<New>) this.sut.getAllNews();
		List<New> newsAdmin = (List<New>) this.sut.getNewsBookReview(user.getUsername());
		List<New> newsOwner = (List<New>) this.sut.getNewsBookReview(user2.getUsername());
		
		Assertions.assertThat(news.size()>newsAdmin.size());
		Assertions.assertThat(news.size()>newsOwner.size());
	}
	
	@ParameterizedTest
	@CsvSource({
		"1,2,3","4,4,5"
	})
	void shouldGetBooksFromNewsId(int newId, int bookId1, int bookId2) {
		List<Book> books = (List<Book>) this.sut.getBooksFromNews(newId);
		Assertions.assertThat(books.get(0).getId() == bookId1);
		Assertions.assertThat(books.get(1).getId() == bookId2);
	}

	@ParameterizedTest
	@CsvSource({
		"1","2"
	})
	void shouldDeteteNewById(int newId) {
		this.sut.deleteById(newId);
		Assertions.assertThat(this.sut.getNewById(newId)).isNull();
	}

	@ParameterizedTest
	@CsvSource({
		"1,4","2,3"
	})
	void shouldNotAddDuplicatedBookInNew(int newId, int bookId) {
		this.sut.saveBookInNew(newId, bookId);
		this.sut.saveBookInNew(newId, bookId);
		BookInNew book = this.bookInNewService.getByNewIdBookId(newId, bookId);
		Assertions.assertThat(book).isNotNull();

	}

	@ParameterizedTest
	@CsvSource({
		"1,2","4,4"
	})
	void shouldDeleteBookInNew(int newId, int bookId) throws DataAccessException, CantDeleteBookInNewException {
		this.sut.deleteBookInNew(newId, bookId);
	}

	@ParameterizedTest
	@CsvSource({
		"2,11","3,1"
	})
	void shouldNotDeleteBookInNew(int newId, int bookId) {
		try {
			this.sut.deleteBookInNew(newId, bookId);

		} catch (CantDeleteBookInNewException e) {
			Assertions.assertThat(e.getCause());
		}
	}

	@ParameterizedTest
	@CsvSource({
		"Name 1","Name 2"
	})
	public void shouldSaveNew(String name) {
		New neew = new New();
		neew.setBody("body");
		LocalDate fecha = LocalDate.of(2019, 12, 02);
		neew.setFecha(fecha);
		neew.setHead("head");
		neew.setName(name);
		neew.setRedactor("redactor");
		this.sut.save(neew);
		New neew2 = this.sut.getNewById(neew.getId());
		Assertions.assertThat(neew2).isNotNull();
		Assertions.assertThat(neew2.getName()).isEqualTo(name);

	}
}
