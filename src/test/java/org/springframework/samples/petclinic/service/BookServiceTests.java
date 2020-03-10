
package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.User;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class BookServiceTests {

	@Autowired
	private BookService bookService;


	@Test
	void shouldFindBooksByTitle() {
		Collection<Book> books = this.bookService.findBookByTitleAuthorGenreISBN("harry");
		Assertions.assertThat(books.size()).isEqualTo(2);
	}

	@Test
	void shouldFindBooksByAuthor() {
		Collection<Book> books = this.bookService.findBookByTitleAuthorGenreISBN("Julia");
		Assertions.assertThat(books.size()).isEqualTo(2);

	}

	@Test
	void shouldFindBooksByGenre() {
		Collection<Book> books = this.bookService.findBookByTitleAuthorGenreISBN("Novel");
		Assertions.assertThat(books.size()).isEqualTo(1);
	}

	@Test
	void shouldFindBooksByISBN() {
		Collection<Book> books = this.bookService.findBookByTitleAuthorGenreISBN("9788466345347");
		Assertions.assertThat(books.size()).isEqualTo(1);
	}

	@Test
	void shouldFindBooksByTitleAuthorGenre() {
		Collection<Book> books = this.bookService.findBookByTitleAuthorGenreISBN("el");
		Assertions.assertThat(books.size()).isEqualTo(3);
	}

	@Test
	void shouldNotFindBooksByTitleAuthorGenre() {
		Collection<Book> books = this.bookService.findBookByTitleAuthorGenreISBN("harrry");
		Assertions.assertThat(books.isEmpty()).isTrue();
	}

	@Test
	void shouldFindBookById() {
		Book book = this.bookService.findBookById(1);

		Assertions.assertThat(book.getTitle()).isEqualTo("IT");
		Assertions.assertThat(book.getAuthor()).isEqualTo("Stephen King");
		Assertions.assertThat(book.getGenre().getName()).isEqualTo("Horror");
		Assertions.assertThat(book.getISBN()).isEqualTo("9788466345347");
		Assertions.assertThat(book.getPages()).isEqualTo(1138);
		Assertions.assertThat(book.getSynopsis()).startsWith("¿Quien");
		Assertions.assertThat(book.getEditorial()).isEqualTo("Viking Press");
		Assertions.assertThat(book.getPublicationDate()).isEqualTo("1986-09-15");
		Assertions.assertThat(book.getVerified()).isTrue();

	}
	@Test
	void shouldVerifyBook() {
//		Collection<GrantedAuthority>l=AuthorityUtils.createAuthorityList("admin");
//		org.springframework.security.core.userdetails.User user=new org.springframework.security.core.userdetails.User("admin","admin",l);
//		this.bookService.verifyBook(6);
		this.bookService.verifyBook(3);
		Assertions.assertThat(this.bookService.findBookById(3).getVerified()).isTrue();
		
	}

	@Test
	void shouldNotChangeVerifiedBook() {
		this.bookService.verifyBook(1);
		Assertions.assertThat(this.bookService.findBookById(1).getVerified()).isTrue();
	}

}
