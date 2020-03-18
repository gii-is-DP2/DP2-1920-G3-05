
package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.ReadBook;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ReadBookServiceTests {

	@Autowired
	private ReadBookService	rbs;
	@Autowired
	private UserService		us;
	@Autowired
	private BookService		bs;


	@Test
	void shouldGetBooksIdsByUsername() {
		List<Integer> ids = new ArrayList<Integer>();
		List<Integer> booksIds = this.rbs.findBooksIdByUser("vet1");
		ids.add(2);
		ids.add(3);
		ids.add(6);
		Assertions.assertThat(booksIds).containsAll(ids);
	}

	@Test
	@Transactional
	void shouldSaveReadBook() {
		Book book = this.bs.findBookById(1);
		User user = this.us.findUserByUsername("vet1");
		ReadBook readBook = new ReadBook();
		readBook.setBook(book);
		readBook.setUser(user);
		this.rbs.save(readBook);

		Assertions.assertThat(this.rbs.findBooksIdByUser("vet1")).contains(1);

	}
}
