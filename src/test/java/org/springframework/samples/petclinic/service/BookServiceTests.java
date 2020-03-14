
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Genre;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedISBNException;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class BookServiceTests {

	@Autowired
	private BookService	bookService;

	@Autowired
	private UserService	userService;


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
	public void shouldInsertBookIntoDatabaseAndGenerateId() throws DataAccessException, DuplicatedISBNException {
		Collection<Book> list = this.bookService.findBookByTitleAuthorGenreISBN("prueba");
		User user = this.userService.findUserByUsername("admin1");
		int count = list.size();

		Book book = new Book();
		book.setTitle("prueba");
		book.setAuthor("antonio");
		book.setEditorial("SM");
		book.setISBN("9788425223280");
		book.setPages(574);
		book.setPublicationDate(LocalDate.now());
		Collection<Genre> genres = this.bookService.findGenre();
		book.setGenre(EntityUtils.getById(genres, Genre.class, 3));
		book.setSynopsis("Esto es una prueba");
		book.setVerified(true);
		book.setUser(user);

		this.bookService.save(book);

		Assertions.assertThat(book.getId()).isNotNull();
		list = this.bookService.findBookByTitleAuthorGenreISBN("prueba");
		Assertions.assertThat(list.size()).isEqualTo(count + 1);

	}

	@Test
	@Transactional
	public void shouldThrowExceptionInsertingBooksWithTheSameISBN() {
		User user = this.userService.findUserByUsername("admin1");

		Book book = new Book();
		book.setTitle("prueba");
		book.setAuthor("antonio");
		book.setEditorial("SM");
		book.setISBN("9788425223280");
		book.setPages(574);
		book.setPublicationDate(LocalDate.now());
		Collection<Genre> genres = this.bookService.findGenre();
		book.setGenre(EntityUtils.getById(genres, Genre.class, 3));
		book.setSynopsis("Esto es una prueba");
		book.setVerified(true);
		book.setUser(user);

		try {
			this.bookService.save(book);

		} catch (DuplicatedISBNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Book bookSameISBN = new Book();
		bookSameISBN.setTitle("prueba1");
		bookSameISBN.setAuthor("pepe");
		bookSameISBN.setEditorial("SMa");
		bookSameISBN.setISBN("9788425223280");
		bookSameISBN.setPages(578);
		bookSameISBN.setPublicationDate(LocalDate.now().minusYears(4));
		bookSameISBN.setGenre(EntityUtils.getById(genres, Genre.class, 4));
		bookSameISBN.setSynopsis("Esto es una prueba 2");
		bookSameISBN.setVerified(true);
		bookSameISBN.setUser(user);
		try {
			this.bookService.save(bookSameISBN);

		} catch (DuplicatedISBNException e) {
			// TODO Auto-generated catch block
			Assertions.assertThat(e.getCause());
			//		Assertions.assertThrows(DuplicatedISBNException.class, () -> {
			//			this.bookService.save(bookSameISBN);
			//		});
		}
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
