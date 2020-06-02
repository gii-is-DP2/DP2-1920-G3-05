
package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.BookInNew;
import org.springframework.samples.petclinic.model.New;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.exceptions.CantDeleteBookInNewException;
import org.springframework.samples.petclinic.service.exceptions.CantShowNewReviewException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE) class NewServiceTest {

	@Autowired
	private NewService			sut;

	@Autowired
	private BookInNewService	bookInNewService;
	
	@Autowired
	private UserService 		userService;


	@ParameterizedTest
	@CsvSource({
		"2,1",
		"7,0"
	})
	void shouldGetNewsIdsFromBookId(int bookId, int results) {
		List<Integer> newsIds = this.sut.getNewsFromBook(bookId);
		Assertions.assertThat(newsIds.size()).isEqualTo(results);
	}

	@ParameterizedTest
	@CsvSource({
		"1,2",
		"4,4"
	})
	void shouldNotDeleteNewWithMoreThanOneBook(int newId, int bookId) { //Como hay mas de un libro deberia borrar el libro de la noticia, pero permanecer la noticia
		this.sut.deleteNew(newId, bookId);
		Boolean existsNew = this.sut.existsNewById(newId);
		Assertions.assertThat(existsNew).isTrue();
	}

	@ParameterizedTest
	@CsvSource({
		"2,11",
		"3,1"
	})
	void shouldDeleteNewWithOneBook(int newId, int bookId) { //Como solo hay un libro deberia borrar el libro de la noticia y la propia noticia
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
		Assertions.assertThat(news.get(index).getId()).isEqualTo(newId);
	}

	@ParameterizedTest
	@CsvSource({
		"admin1",
		"owner1",
		"vet1"
	})
	void shouldGetNewBookReview(String username) throws CantShowNewReviewException{
		List<New> news = (List<New>) this.sut.getAllNews();
		List<New> newsUser = (List<New>) this.sut.getNewsBookReview(username);

		Assertions.assertThat(news.size()>=newsUser.size()).isTrue();
	}
	
	@ParameterizedTest
	@CsvSource({
		"admin1",
		"owner1",
		"vet1"
	})
	void shouldGetNewBookReview2(String username) throws CantShowNewReviewException{
		List<New> news = (List<New>) this.sut.getAllNews();
		List<New> newsUser = (List<New>) this.sut.getNewsBookReview2(username);

		Assertions.assertThat(news.size()>=newsUser.size()).isTrue();

	}
	
	@ParameterizedTest
	@CsvSource({
		"reader2"
	})
	void shouldGetNewBookReviewException(String username) {
		assertThrows(CantShowNewReviewException.class, ()-> this.sut.getNewsBookReview(username));
	}

	@ParameterizedTest
	@CsvSource({
		"1,2,3","4,4,5"
	})
	void shouldGetBooksFromNewsId(int newId, int bookId1, int bookId2) {
		List<Book> books = (List<Book>) this.sut.getBooksFromNews(newId);
		Assertions.assertThat(books.get(0).getId()).isEqualTo(bookId1);
		Assertions.assertThat(books.get(1).getId()).isEqualTo(bookId2);
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
	void shouldDeleteBookInNew(int newId, int bookId) throws CantDeleteBookInNewException {
		this.sut.deleteBookInNew(newId, bookId);
		BookInNew book = this.bookInNewService.getByNewIdBookId(newId, bookId);
		Assertions.assertThat(book).isNull();
	}

	@ParameterizedTest
	@CsvSource({
		"2,11","3,1"
	})
	void shouldNotDeleteBookInNew(int newId, int bookId) {
		assertThrows(CantDeleteBookInNewException.class, ()-> this.sut.deleteBookInNew(newId, bookId));
	}

	@ParameterizedTest
	@CsvSource({
		"Name 1","Name 2"
	})
	 void shouldSaveNew(String name) {
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
