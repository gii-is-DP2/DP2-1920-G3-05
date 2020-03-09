
package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class BookServiceTests {

	@Autowired
	private BookService bookService;
	
	@Autowired
	private NewService newService;

	@Autowired
	private MeetingService meetingService;
	
	@Autowired
	private PublicationService publicationService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private BookInNewService bookInNewsService;

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
	void shouldDeleteBookWithNoRelations() {
		int bookId = 6;
		Boolean existsBook = this.bookService.existsBookById(bookId);
		Assertions.assertThat(existsBook).isTrue();
		
		this.bookService.deleteById(bookId);
		
		existsBook = this.bookService.existsBookById(bookId);
		Assertions.assertThat(existsBook).isFalse();		
	}
	
	@Test
	void shouldDeleteBookWithNewOnlyOneBook() {
		int bookId = 11;
		int newId = 2; //Como solo hay 1 libro se borrara la noticia tb
		Boolean existsBook = this.bookService.existsBookById(bookId);
		Boolean existsNew = this.newService.existsNewById(newId); 
		Assertions.assertThat(existsBook).isTrue();
		Assertions.assertThat(existsNew).isTrue();
		
		this.bookService.deleteById(bookId);
		
		existsBook = this.bookService.existsBookById(bookId);
		existsNew = this.newService.existsNewById(newId); 
		Assertions.assertThat(existsBook).isFalse();
		Assertions.assertThat(existsNew).isFalse();
	}
	
	@Test
	void shouldDeleteBookWithNewMoreThanOneBook() {
		int bookId = 2;
		int newId = 1; //Como solo hay 2 libros no se borrara la noticia tb
		List<Integer> booksInNewIds = this.bookInNewsService.getBooksInNewFromNew(newId);
		Boolean existsBook = this.bookService.existsBookById(bookId);
		Boolean existsNew = this.newService.existsNewById(newId); 
		
		Assertions.assertThat(existsBook).isTrue();
		Assertions.assertThat(existsNew).isTrue();
		Assertions.assertThat(booksInNewIds).hasSize(2).contains(bookId);
		
		this.bookService.deleteById(bookId);
		
		existsBook = this.bookService.existsBookById(bookId);
		existsNew = this.newService.existsNewById(newId); 
		booksInNewIds = this.bookInNewsService.getBooksInNewFromNew(newId);
		
		Assertions.assertThat(existsBook).isFalse();
		Assertions.assertThat(existsNew).isTrue();
		Assertions.assertThat(booksInNewIds).hasSize(1).doesNotContain(bookId);
	}

	@Test
	void shouldDeleteBookWithMeeting() {
		int bookId = 10;
		int meetingId = 4;
		Boolean existsBook = this.bookService.existsBookById(bookId);
		Boolean existsMeeting = this.meetingService.existsMeetingById(meetingId);
		Assertions.assertThat(existsBook).isTrue();
		Assertions.assertThat(existsMeeting).isTrue();
		
		this.bookService.deleteById(bookId);
		
		existsBook = this.bookService.existsBookById(bookId);
		existsMeeting = this.meetingService.existsMeetingById(meetingId);
		Assertions.assertThat(existsBook).isFalse();
		Assertions.assertThat(existsMeeting).isFalse();
	}
	
	@Test
	void shouldDeleteBookWithReview() {
		int bookId = 4;
		int reviewId = 6;
		Boolean existsBook = this.bookService.existsBookById(bookId);
		Boolean existsReview = this.reviewService.existsReviewById(reviewId);
		Assertions.assertThat(existsBook).isTrue();
		Assertions.assertThat(existsReview).isTrue();
		
		this.bookService.deleteById(bookId);
		
		existsBook = this.bookService.existsBookById(bookId);
		existsReview = this.reviewService.existsReviewById(reviewId);
		Assertions.assertThat(existsBook).isFalse();
		Assertions.assertThat(existsReview).isFalse();
	}
	
	@Test
	void shouldDeleteBookWithPublication() {
		int bookId = 8;
		int publicationId = 5;
		Boolean existsBook = this.bookService.existsBookById(bookId);
		Boolean existsPublication = this.publicationService.existsPublicationById(publicationId);
		Assertions.assertThat(existsBook).isTrue();
		Assertions.assertThat(existsPublication).isTrue();

		this.bookService.deleteById(bookId);
		
		existsBook = this.bookService.existsBookById(bookId);
		existsPublication = this.publicationService.existsPublicationById(publicationId);
		Assertions.assertThat(existsBook).isFalse();
		Assertions.assertThat(existsPublication).isFalse();
	}
	
	@Test
	void shouldDeleteBookWithEverything() {
		int bookId = 1;
		int newId = 3; //Solo un libro --> se borra noticia
		int reviewId1 = 1;
		int reviewId2 = 2;
		int reviewId3 = 3;
		int publicationId1 = 1;
		int publicationId2 = 2;
		int meetingId = 2;
		
		Boolean existsBook = this.bookService.existsBookById(bookId);
		Boolean existsNew = this.newService.existsNewById(newId);
		Boolean existsReview1 = this.reviewService.existsReviewById(reviewId1);
		Boolean existsReview2 = this.reviewService.existsReviewById(reviewId2);
		Boolean existsReview3 = this.reviewService.existsReviewById(reviewId3);
		Boolean existsPublication1 = this.publicationService.existsPublicationById(publicationId1);
		Boolean existsPublication2 = this.publicationService.existsPublicationById(publicationId2);
		Boolean existsMeeting = this.meetingService.existsMeetingById(meetingId);
		
		Assertions.assertThat(existsBook).isTrue();
		Assertions.assertThat(existsNew).isTrue();
		Assertions.assertThat(existsReview1).isTrue();
		Assertions.assertThat(existsReview2).isTrue();
		Assertions.assertThat(existsReview3).isTrue();
		Assertions.assertThat(existsPublication1).isTrue();
		Assertions.assertThat(existsPublication2).isTrue();
		Assertions.assertThat(existsMeeting).isTrue();

		this.bookService.deleteById(bookId);
		
		existsBook = this.bookService.existsBookById(bookId);
		existsBook = this.bookService.existsBookById(bookId);
		existsNew = this.newService.existsNewById(newId);
		existsReview1 = this.reviewService.existsReviewById(reviewId1);
		existsReview2 = this.reviewService.existsReviewById(reviewId2);
		existsReview3 = this.reviewService.existsReviewById(reviewId3);
		existsPublication1 = this.publicationService.existsPublicationById(publicationId1);
		existsPublication2 = this.publicationService.existsPublicationById(publicationId2);
		existsMeeting = this.meetingService.existsMeetingById(meetingId);
		
		Assertions.assertThat(existsBook).isFalse();
		Assertions.assertThat(existsNew).isFalse();
		Assertions.assertThat(existsReview1).isFalse();
		Assertions.assertThat(existsReview2).isFalse();
		Assertions.assertThat(existsReview3).isFalse();
		Assertions.assertThat(existsPublication1).isFalse();
		Assertions.assertThat(existsPublication2).isFalse();
		Assertions.assertThat(existsMeeting).isFalse();
	}
}
