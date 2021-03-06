
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Genre;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedISBNException;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class BookServiceTests {

	@Autowired
	private BookService	sut;
    
	@Autowired
	private NewService newService;

	@Autowired
	private UserService	userService;
    
    
	@Autowired
	private MeetingService meetingService;
	
	@Autowired
	private PublicationService ationService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private BookInNewService bookInNewsService;


	@ParameterizedTest
	@CsvSource({
		"harry,2","Julia,2","Novel,1","9788466345347,1","el,4","harrry,0"
	})
	void shouldFindBooksByTitleAuthorGenre(String title,int size) {
		Collection<Book> books = this.sut.findBookByTitleAuthorGenreISBN(title);
		Assertions.assertThat(books.size()).isEqualTo(size);
	}

	@ParameterizedTest
	@CsvSource({
		"prueba,admin1,13"
	})
	void shouldInsertBookIntoDatabaseAndGenerateIdAdmin(String titleBook, String username, int futureBookId) throws DuplicatedISBNException {

		Collection<Book> list = this.sut.findBookByTitleAuthorGenreISBN(titleBook);
		User user = this.userService.findUserByUsername(username);
		int count = list.size();

		Book book = new Book();
		book.setTitle("prueba");
		book.setAuthor("antonio");
		book.setEditorial("SM");
		book.setISBN("9788425223280");
		book.setPages(574);
		book.setPublicationDate(LocalDate.now());
		Collection<Genre> genres = this.sut.findGenre();
		book.setGenre(EntityUtils.getById(genres, Genre.class, 3));
		book.setSynopsis("Esto es una prueba");
		book.setUser(user);

		this.sut.save(book);
		
		//Le toca el id 13
		Assertions.assertThat(futureBookId).isNotNull();
		Assertions.assertThat(book.getVerified()).isTrue();
		list = this.sut.findBookByTitleAuthorGenreISBN("prueba");
		Assertions.assertThat(list.size()).isEqualTo(count + 1);

	}
	@ParameterizedTest
	@CsvSource({
		"prueba,owner1,14"
	})
	void shouldInsertBookIntoDatabaseAndGenerateIdNoAdmin(String titleBook, String username, int futureBookId) throws DuplicatedISBNException {

		Collection<Book> list = this.sut.findBookByTitleAuthorGenreISBN(titleBook);
		User user = this.userService.findUserByUsername(username);
		int count = list.size();

		Book book = new Book();
		book.setTitle("prueba");
		book.setAuthor("antonio");
		book.setEditorial("SM");
		book.setISBN("9788425223280");
		book.setPages(574);
		book.setPublicationDate(LocalDate.now());
		Collection<Genre> genres = this.sut.findGenre();
		book.setGenre(EntityUtils.getById(genres, Genre.class, 3));
		book.setSynopsis("Esto es una prueba");
		book.setUser(user);

		this.sut.save(book);
		

		Assertions.assertThat(futureBookId).isNotNull();
		Assertions.assertThat(book.getVerified()).isFalse();
		list = this.sut.findBookByTitleAuthorGenreISBN("prueba");
		Assertions.assertThat(list.size()).isEqualTo(count + 1);

	}

	@ParameterizedTest
	@CsvSource({
		"admin1"
	})
	 void shouldThrowExceptionInsertingBooksWithTheSameISBN(String username) {
		User user = this.userService.findUserByUsername(username);

		Book book = new Book();
		book.setTitle("prueba");
		book.setAuthor("antonio");
		book.setEditorial("SM");
		book.setISBN("9788425223280");
		book.setPages(574);
		book.setPublicationDate(LocalDate.now());
		Collection<Genre> genres = this.sut.findGenre();
		book.setGenre(EntityUtils.getById(genres, Genre.class, 3));
		book.setSynopsis("Esto es una prueba");
		book.setVerified(true);
		book.setUser(user);

		try {
			this.sut.save(book);

		} catch (DuplicatedISBNException e) {
			
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
			this.sut.save(bookSameISBN);

		} catch (DuplicatedISBNException e) {
			//Assertions.assertThat(e.getCause());
					assertThrows(DuplicatedISBNException.class, () -> {
						this.sut.save(bookSameISBN);
					});
		}
	}

	@ParameterizedTest
	@CsvSource({
		"1", "2", "3"
	})
	 void shouldUpdateBookName(final int bookId) throws Exception {
		Book book = this.sut.findBookById(bookId);
		String oldTitle = book.getTitle();

		String newTitle = oldTitle + "X";
		book.setTitle(newTitle);
		this.sut.save(book);

		book = this.sut.findBookById(bookId);
		Assertions.assertThat(book.getTitle()).isEqualTo(newTitle);

	}
	@ParameterizedTest
	@CsvSource({
		"1, Fantasy",
		"4, Contemporary"
	})
	void shouldFindAllGenres(int genreId, String name) {
		Collection<Genre> bookGenre = this.sut.findGenre();

		Genre genre1 = EntityUtils.getById(bookGenre, Genre.class, genreId);
		Assertions.assertThat(genre1.getName()).isEqualTo(name);

	}
	
	@ParameterizedTest
	@CsvSource({
		"Romance", "Fiction", "Horror"
	})
	 void shouldFindGenre(String genre) throws Exception {
		Genre genre3 = this.sut.findGenreByName(genre);

		Assertions.assertThat(genre).isEqualTo(genre3.getName());

	}
	
	@ParameterizedTest
	@CsvSource({
		"6,admin1"
	})
	void adminCanDeleteBookWithNoRelations(int bookId, String username) {
		Boolean existsBook = this.sut.existsBookById(bookId);
		Assertions.assertThat(existsBook).isTrue();
		
		this.sut.deleteById(bookId, username);
		
		existsBook = this.sut.existsBookById(bookId);
		Assertions.assertThat(existsBook).isFalse();		
	}
	
	@ParameterizedTest
	@CsvSource({
		"11,2,admin1",
		"1,3,admin1"
	})
	void adminCanDeleteBookAndNew(int bookId, int newId, String username) {
		this.sut.deleteById(bookId, username);
		Boolean existsBook = this.sut.existsBookById(bookId);
		Boolean existsNew = this.newService.existsNewById(newId); 
		Assertions.assertThat(existsBook).isFalse();
		Assertions.assertThat(existsNew).isFalse();
	}
	
	@ParameterizedTest
	@CsvSource({
		"2,1,admin1",
		"4,4,admin1",
		"6,5,admin1"
	})
	void adminCanDeleteBookButNoNew(int bookId, int newId, String username) {
		this.sut.deleteById(bookId, username);
		
		Boolean existsBook = this.sut.existsBookById(bookId);
		Boolean existsNew = this.newService.existsNewById(newId); 
		List<Integer> booksInNewIds = this.bookInNewsService.getBooksInNewFromNew(newId);
		
		Assertions.assertThat(existsBook).isFalse();
		Assertions.assertThat(existsNew).isTrue();
		Assertions.assertThat(booksInNewIds).hasSize(1).doesNotContain(bookId);
	}
	
	@ParameterizedTest
	@CsvSource({
		"10,4,admin1",
		"2,3,admin1",
		"1,2,admin1"
	})
	void adminCanDeleteBookWithMeeting(int bookId, int meetingId, String username) {
		this.sut.deleteById(bookId, username);
		
		Boolean existsBook = this.sut.existsBookById(bookId);
		Boolean existsMeeting = this.meetingService.existsMeetingById(meetingId);
		Assertions.assertThat(existsBook).isFalse();
		Assertions.assertThat(existsMeeting).isFalse();
	}
	
	@ParameterizedTest
	@CsvSource({
		"4,6,admin1",
		"3,5,admin1",
		"5,7,admin1"
	})
	void adminCanDeleteBookWithReview(int bookId, int reviewId, String username) {
		this.sut.deleteById(bookId, username);
		
		Boolean existsBook = this.sut.existsBookById(bookId);
		Boolean existsReview = this.reviewService.existsReviewById(reviewId);
		Assertions.assertThat(existsBook).isFalse();
		Assertions.assertThat(existsReview).isFalse();
	}
	
	@ParameterizedTest
	@CsvSource({
		"8,5,admin1",
		"10,6,admin1",
		"7,4,admin1"
	})
	void adminCanDeleteBookWithPublication() {
		int bookId = 8;
		int ationId = 5;
		String username = "admin1";

		this.sut.deleteById(bookId, username);
		
		Boolean existsBook = this.sut.existsBookById(bookId);
		Boolean existsPublication = this.ationService.existsPublicationById(ationId);
		Assertions.assertThat(existsBook).isFalse();
		Assertions.assertThat(existsPublication).isFalse();
	}
	
	@ParameterizedTest
	@CsvSource({
		"1,3,1,2,3,1,2,2,admin1"
	})
	void adminCanDeleteBookWithEverything(int bookId, int newId, int reviewId1, int reviewId2, int reviewId3, int ationId1, int ationId2, int meetingId, String username) {
		this.sut.deleteById(bookId, username);
		
		Boolean existsBook = this.sut.existsBookById(bookId);
		Boolean existsNew = this.newService.existsNewById(newId);
		Boolean existsReview1 = this.reviewService.existsReviewById(reviewId1);
		Boolean existsReview2 = this.reviewService.existsReviewById(reviewId2);
		Boolean existsReview3 = this.reviewService.existsReviewById(reviewId3);
		Boolean existsPublication1 = this.ationService.existsPublicationById(ationId1);
		Boolean existsPublication2 = this.ationService.existsPublicationById(ationId2);
		Boolean existsMeeting = this.meetingService.existsMeetingById(meetingId);
		
		Assertions.assertThat(existsBook).isFalse();
		Assertions.assertThat(existsNew).isFalse();
		Assertions.assertThat(existsReview1).isFalse();
		Assertions.assertThat(existsReview2).isFalse();
		Assertions.assertThat(existsReview3).isFalse();
		Assertions.assertThat(existsPublication1).isFalse();
		Assertions.assertThat(existsPublication2).isFalse();
		Assertions.assertThat(existsMeeting).isFalse();
	}
    
	@ParameterizedTest
	@CsvSource({
		"3",
		"6",
		"9"
		
	})
	void shouldVerifyBook(int id) {
//		Collection<GrantedAuthority>l=AuthorityUtils.createAuthorityList("admin");
//		org.springframework.security.core.userdetails.User user=new org.springframework.security.core.userdetails.User("admin","admin",l);
//		this.bookService.verifyBook(6);
		this.sut.verifyBook(id);
		Assertions.assertThat(this.sut.findBookById(id).getVerified()).isTrue();
		
	}
    
	@ParameterizedTest
	@CsvSource({
		"1",
		"2",
		"4"
		
	})
	void shouldNotChangeVerifiedBook(int id) {
		this.sut.verifyBook(id);
		Assertions.assertThat(this.sut.findBookById(id).getVerified()).isTrue();
	}

	@ParameterizedTest
	@CsvSource({
		"owner1,3",
		"owner1,6",
		"vet1,9"
		
	})
	void canEditCauseIsMineAndNotVerified(String username,int bookId){
		Boolean canEdit = this.sut.canEditBook(bookId, username);
		Assertions.assertThat(canEdit).isTrue();
	}

	@ParameterizedTest
	@CsvSource({
		"owner1,1",
		"vet1,7",
		"owner1,4"
		
	})
	void cantEditMyBookCauseIsVerified(String username,int bookId){
		Boolean canEdit = this.sut.canEditBook(bookId, username);
		Assertions.assertThat(canEdit).isFalse();
	}

	@ParameterizedTest
	@CsvSource({
		"3",
		"6",
		"9"
		
	})
	void adminCanEditOthersUnverifiedBook(int bookId){
		String username = "admin1";
		Boolean canEdit = this.sut.canEditBook(bookId, username);
		Assertions.assertThat(canEdit).isTrue();
	}

	@ParameterizedTest
	@CsvSource({
		"1",
		"5",
		"4"
		
	})
	void adminCanEditOthersVerifiedBook(){
		String username = "admin1";
		int bookId = 1;
		Boolean canEdit = this.sut.canEditBook(bookId, username);
		Assertions.assertThat(canEdit).isTrue();
	}
	
	@ParameterizedTest
	@CsvSource({
		"admin1",
		"reader1"
		
	})
	void shouldHaveVerifiedBooks(){
		String username = "admin1";
		List<Boolean> verified= this.sut.getVerifiedFromBooksByUsername(username);
		Assertions.assertThat(verified).allMatch(i->i==true);
	}
	
}
