
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Genre;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	public void shouldUpdateBookName() throws Exception {
		Book book3 = this.bookService.findBookById(3);
		String oldTitle = book3.getTitle();

		String newTitle = oldTitle + "X";
		book3.setTitle(newTitle);
		this.bookService.save(book3);

		book3 = this.bookService.findBookById(3);
		Assertions.assertThat(book3.getTitle()).isEqualTo(newTitle);

	}
	@Test
	void shouldFindAllGenres() {
		Collection<Genre> bookGenre = this.bookService.findBookGenres();

		Genre genre1 = EntityUtils.getById(bookGenre, Genre.class, 1);
		Assertions.assertThat(genre1.getName()).isEqualTo("Fantasy");
		Genre genre4 = EntityUtils.getById(bookGenre, Genre.class, 4);
		Assertions.assertThat(genre4.getName()).isEqualTo("Contemporary");
	}
	@Test
	@Transactional
	public void shouldFindGenre() throws Exception {
		Genre genre3 = this.bookService.findGenreByName("Romance");

		Assertions.assertThat("Romance").isEqualTo(genre3.getName());

	}
}
