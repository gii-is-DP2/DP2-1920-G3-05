
package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.ReadBook;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class ReadBookServiceTests {

	@Autowired
	private ReadBookService	rbs;
	@Autowired
	private UserService		us;
	@Autowired
	private BookService		bs;


	@ParameterizedTest
	@ValueSource(strings = {
		"vet1, 2, 3, 6", "owner1, 1, 4, 5", "admin1, 8, 9, 10"
	})
	void shouldGetBooksIdsByUsername(final String arg) {
		String[] objetos = arg.split("\\s*,\\s*");
		List<Integer> booksIds = this.rbs.findBooksIdByUser(objetos[0]);
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(Integer.parseInt(objetos[1]));
		ids.add(Integer.parseInt(objetos[2]));
		ids.add(Integer.parseInt(objetos[3]));
		Assertions.assertThat(booksIds).containsAll(ids);
	}

	@ParameterizedTest
	@CsvSource({
		"1,vet1", "4,admin1", "8,reader1"
	})
	void shouldSaveReadBook(final int id, final String username) {
		Book book = this.bs.findBookById(id);
		User user = this.us.findUserByUsername(username);
		ReadBook readBook = new ReadBook();
		readBook.setBook(book);
		readBook.setUser(user);
		this.rbs.save(readBook);

		Assertions.assertThat(this.rbs.findBooksIdByUser(username)).contains(id);

	}

	@ParameterizedTest
	@CsvSource({
		"1,2,6", "11,7,10", "2,9,7"
	})
	void shouldGetTopReadBooks(final int id1, final int id2, final int id3) {
		List<Integer> ids = new ArrayList<Integer>();
		List<Integer> booksIds = this.rbs.topReadBooks();
		ids.add(id1);
		ids.add(id2);
		ids.add(id3);
		Assertions.assertThat(booksIds).containsAll(ids);
	}

	@ParameterizedTest
	@CsvSource({
		"1,owner1", "7,admin1", "11,reader1"
	})
	void shoulReadBook(final int bookId, final String username) {
		Boolean ownerReadBook = this.rbs.esReadBook(bookId, username);
		Assertions.assertThat(ownerReadBook).isTrue();
	}

	@ParameterizedTest
	@CsvSource({
		"10,vet1", "2,admin1", "8,reader1"
	})
	void shouldChangeTopReadBooks(final int id, final String username) {
		Book book = this.bs.findBookById(id);
		User user = this.us.findUserByUsername(username);
		ReadBook readBook = new ReadBook();
		readBook.setBook(book);
		readBook.setUser(user);
		this.rbs.save(readBook);
		List<Integer> booksIds = this.rbs.topReadBooks();
		Assertions.assertThat(booksIds).contains(id);
	}

	@ParameterizedTest
	@CsvSource({
		"7,vet1", "2,admin1", "3,reader1"
	})
	void shoulNotReadBook(final int bookId, final String username) {
		Boolean adminReadBook = this.rbs.esReadBook(bookId, username);
		Assertions.assertThat(adminReadBook).isFalse();
	}

	@ParameterizedTest
	@CsvSource({
		"1,4", "2,2", "6,3"
	})
	void shouldDeleteReadBooksByBookId(final int bookId, final int readBookId) {
		this.rbs.deleteReadBookByBookId(bookId);
		Boolean exitsReadBook = this.rbs.existsReadBook(readBookId);
		Assertions.assertThat(exitsReadBook).isFalse();
	}
}
