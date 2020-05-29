package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.WishedBook;
import org.springframework.samples.petclinic.service.exceptions.ReadOrWishedBookException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
 class WishedBookServiceTests {

	@Autowired
	private WishedBookService	sut;
	@Autowired
	private BookService	bookService;
	@Autowired
	private UserService	userService;
	
	@ParameterizedTest
	@CsvSource({
		"2,admin1,3",
		"4,vet1,3",
		"10,owner1,1"
	})
	void shouldAddToWishList(Integer bookId,String username,Integer expectedNumBooks) throws DataAccessException, ReadOrWishedBookException {
		Book book=bookService.findBookById(bookId);
		User user=userService.findUserByUsername(username);
		WishedBook wishedBook= new WishedBook();
		wishedBook.setBook(book);
		wishedBook.setUser(user);
		sut.save(wishedBook);
		Assertions.assertThat(sut.findBooksIdByUser(user.getUsername()).size()).isEqualTo(expectedNumBooks);
	}
	
	@ParameterizedTest
	@CsvSource({
		"3,vet1",
		"5,owner1",
		"8,admin1"
	})
	void shouldNotAddReadBook(Integer bookId,String username) {
		Book book=bookService.findBookById(bookId);
		User user=userService.findUserByUsername(username);
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
	
	@ParameterizedTest
	@CsvSource({
		"3,admin1",
		"4,admin1",
		"10,vet1"
	})
	void shouldNotAddWishedBook(Integer bookId,String username) {
		Book book=bookService.findBookById(bookId);
		User user=userService.findUserByUsername(username);
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
	
	@ParameterizedTest
	@CsvSource({
		"4,admin1,1",
		"3,admin1,1",
		"10,vet1,1"
	})
	void shouldDeleteFromWishList(Integer bookId,String username,Integer expectedNumBooks) {
		sut.deleteByBookId(bookId);
		Collection<Integer> books=sut.findBooksIdByUser(username);
		Assertions.assertThat(books.size()).isEqualTo(expectedNumBooks);
	}
}
