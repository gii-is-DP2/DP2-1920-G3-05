package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.WishedBook;
import org.springframework.samples.petclinic.service.exceptions.ReadOrWishedBookException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class WishedBookServiceTests {

	@Autowired
	private WishedBookService	sut;
	@Autowired
	private BookService	bookService;
	@Autowired
	private UserService	userService;
	
	@Test
	void shouldAddToWishList() throws DataAccessException, ReadOrWishedBookException {
		Book book=bookService.findBookById(2);
		User user=userService.findUserByUsername("admin1");
		WishedBook wishedBook= new WishedBook();
		wishedBook.setBook(book);
		wishedBook.setUser(user);
		sut.save(wishedBook);
		Assertions.assertThat(sut.findBooksIdByUser("admin1").size()).isEqualTo(3);
	}
	
	@Test
	void shouldNotAddReadBook() {
		Book book=bookService.findBookById(3);
		User user=userService.findUserByUsername("vet1");
		WishedBook wishedBook= new WishedBook();
		wishedBook.setBook(book);
		wishedBook.setUser(user);
		try {
			sut.save(wishedBook);
		} catch (ReadOrWishedBookException e) {
			// TODO Auto-generated catch block
			Assertions.assertThat(e.getCause());
		}
	}
	
	@Test
	void shouldNotAddWishedBook() {
		Book book=bookService.findBookById(1);
		User user=userService.findUserByUsername("admin1");
		WishedBook wishedBook= new WishedBook();
		wishedBook.setBook(book);
		wishedBook.setUser(user);
		try {
			sut.save(wishedBook);
		} catch (ReadOrWishedBookException e) {
			// TODO Auto-generated catch block
			Assertions.assertThat(e.getCause());
		}
	}
	
	@Test
	void shouldDeleteFromWishList() {
		sut.deleteByBookId(4);
		Collection<Integer> books=sut.findBooksIdByUser("admin1");
		Assertions.assertThat(books.size()).isEqualTo(1);
	}
}
