
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
	private BookService	sut;
    
	@Autowired
	private NewService newService;

	@Autowired
	private UserService	userService;
    
    
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
		Collection<Book> books = this.sut.findBookByTitleAuthorGenreISBN("harry");
		Assertions.assertThat(books.size()).isEqualTo(2);
	}

	@Test
	void shouldFindBooksByAuthor() {
		Collection<Book> books = this.sut.findBookByTitleAuthorGenreISBN("Julia");
		Assertions.assertThat(books.size()).isEqualTo(2);

	}

	@Test
	void shouldFindBooksByGenre() {
		Collection<Book> books = this.sut.findBookByTitleAuthorGenreISBN("Novel");
		Assertions.assertThat(books.size()).isEqualTo(1);
	}

	@Test
	void shouldFindBooksByISBN() {
		Collection<Book> books = this.sut.findBookByTitleAuthorGenreISBN("9788466345347");
		Assertions.assertThat(books.size()).isEqualTo(1);
	}

	@Test
	void shouldFindBooksByTitleAuthorGenre() {
		Collection<Book> books = this.sut.findBookByTitleAuthorGenreISBN("el");
		Assertions.assertThat(books.size()).isEqualTo(3);
	}

	@Test
	void shouldNotFindBooksByTitleAuthorGenre() {
		Collection<Book> books = this.sut.findBookByTitleAuthorGenreISBN("harrry");
		Assertions.assertThat(books.isEmpty()).isTrue();
	}

	@ParameterizedTest
	@CsvSource({
		"prueba,admin1,13"
	})
	public void shouldInsertBookIntoDatabaseAndGenerateIdAdmin(String titleBook, String username, int futureBookId) throws DataAccessException, DuplicatedISBNException {
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
	public void shouldInsertBookIntoDatabaseAndGenerateIdNoAdmin(String titleBook, String username, int futureBookId) throws DataAccessException, DuplicatedISBNException {
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
	public void shouldThrowExceptionInsertingBooksWithTheSameISBN(String username) {
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
			// TODO Auto-generated catch block
			//Assertions.assertThat(e.getCause());
					assertThrows(DuplicatedISBNException.class, () -> {
						this.sut.save(bookSameISBN);
					});
		}
	}

	@Test
	@Transactional
	public void shouldUpdateBookName() throws Exception {
		Book book3 = this.sut.findBookById(3);
		String oldTitle = book3.getTitle();

		String newTitle = oldTitle + "X";
		book3.setTitle(newTitle);
		this.sut.save(book3);

		book3 = this.sut.findBookById(3);
		Assertions.assertThat(book3.getTitle()).isEqualTo(newTitle);

	}
	@Test
	void shouldFindAllGenres() {
		Collection<Genre> bookGenre = this.sut.findGenre();

		Genre genre1 = EntityUtils.getById(bookGenre, Genre.class, 1);
		Assertions.assertThat(genre1.getName()).isEqualTo("Fantasy");
		Genre genre4 = EntityUtils.getById(bookGenre, Genre.class, 4);
		Assertions.assertThat(genre4.getName()).isEqualTo("Contemporary");
	}
	@Test
	@Transactional
	public void shouldFindGenre() throws Exception {
		Genre genre3 = this.sut.findGenreByName("Romance");

		Assertions.assertThat("Romance").isEqualTo(genre3.getName());

	}
	
	@Test
	void adminCanDeleteBookWithNoRelations() {
		int bookId = 6;
		String username = "admin1";
		Boolean existsBook = this.sut.existsBookById(bookId);
		Assertions.assertThat(existsBook).isTrue();
		
		this.sut.deleteById(bookId, username);
		
		existsBook = this.sut.existsBookById(bookId);
		Assertions.assertThat(existsBook).isFalse();		
	}
	
	@Test
	void adminCanDeleteBookAndNew() {
		int bookId = 11;
		int newId = 2; //Como solo hay 1 libro se borrara la noticia tambien
		String username = "admin1";

		this.sut.deleteById(bookId, username);
		
		Boolean existsBook = this.sut.existsBookById(bookId);
		Boolean existsNew = this.newService.existsNewById(newId); 
		Assertions.assertThat(existsBook).isFalse();
		Assertions.assertThat(existsNew).isFalse();
	}
	
	@Test
	void adminCanDeleteBookButNoNew() {
		int bookId = 2;
		int newId = 1; //Como solo hay 2 libros no se borrara la noticia tambien
		String username = "admin1";

		this.sut.deleteById(bookId, username);
		
		Boolean existsBook = this.sut.existsBookById(bookId);
		Boolean existsNew = this.newService.existsNewById(newId); 
		List<Integer> booksInNewIds = this.bookInNewsService.getBooksInNewFromNew(newId);
		
		Assertions.assertThat(existsBook).isFalse();
		Assertions.assertThat(existsNew).isTrue();
		Assertions.assertThat(booksInNewIds).hasSize(1).doesNotContain(bookId);
	}
	
	@Test
	void adminCanDeleteBookWithMeeting() {
		int bookId = 10;
		int meetingId = 4;
		String username = "admin1";

		this.sut.deleteById(bookId, username);
		
		Boolean existsBook = this.sut.existsBookById(bookId);
		Boolean existsMeeting = this.meetingService.existsMeetingById(meetingId);
		Assertions.assertThat(existsBook).isFalse();
		Assertions.assertThat(existsMeeting).isFalse();
	}
	
	@Test
	void adminCanDeleteBookWithReview() {
		int bookId = 4;
		int reviewId = 6;
		String username = "admin1";

		this.sut.deleteById(bookId, username);
		
		Boolean existsBook = this.sut.existsBookById(bookId);
		Boolean existsReview = this.reviewService.existsReviewById(reviewId);
		Assertions.assertThat(existsBook).isFalse();
		Assertions.assertThat(existsReview).isFalse();
	}
	
	@Test
	void adminCanDeleteBookWithPublication() {
		int bookId = 8;
		int publicationId = 5;
		String username = "admin1";

		this.sut.deleteById(bookId, username);
		
		Boolean existsBook = this.sut.existsBookById(bookId);
		Boolean existsPublication = this.publicationService.existsPublicationById(publicationId);
		Assertions.assertThat(existsBook).isFalse();
		Assertions.assertThat(existsPublication).isFalse();
	}
	
	@Test
	void adminCanDeleteBookWithEverything() {
		int bookId = 1;
		int newId = 3; //Solo un libro --> se borra noticia
		int reviewId1 = 1;
		int reviewId2 = 2;
		int reviewId3 = 3;
		int publicationId1 = 1;
		int publicationId2 = 2;
		int meetingId = 2;
		String username = "admin1";

		this.sut.deleteById(bookId, username);
		
		Boolean existsBook = this.sut.existsBookById(bookId);
		Boolean existsNew = this.newService.existsNewById(newId);
		Boolean existsReview1 = this.reviewService.existsReviewById(reviewId1);
		Boolean existsReview2 = this.reviewService.existsReviewById(reviewId2);
		Boolean existsReview3 = this.reviewService.existsReviewById(reviewId3);
		Boolean existsPublication1 = this.publicationService.existsPublicationById(publicationId1);
		Boolean existsPublication2 = this.publicationService.existsPublicationById(publicationId2);
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
    
    @Test
	void shouldVerifyBook() {
//		Collection<GrantedAuthority>l=AuthorityUtils.createAuthorityList("admin");
//		org.springframework.security.core.userdetails.User user=new org.springframework.security.core.userdetails.User("admin","admin",l);
//		this.bookService.verifyBook(6);
		this.sut.verifyBook(3);
		Assertions.assertThat(this.sut.findBookById(3).getVerified()).isTrue();
		
	}
    
	@Test
	void shouldNotChangeVerifiedBook() {
		this.sut.verifyBook(1);
		Assertions.assertThat(this.sut.findBookById(1).getVerified()).isTrue();
	}

	@Test
	void canEditCauseIsMineAndNotVerified(){
		String username = "owner1";
		int bookId = 3;
		Boolean canEdit = this.sut.canEditBook(bookId, username);
		Assertions.assertThat(canEdit).isTrue();
	}

	@Test
	void cantEditMyBookCauseIsVerified(){
		String username = "owner1";
		int bookId = 1;
		Boolean canEdit = this.sut.canEditBook(bookId, username);
		Assertions.assertThat(canEdit).isFalse();
	}

	@Test
	void adminCanEditOthersUnverifiedBook(){
		String username = "admin1";
		int bookId = 3;
		Boolean canEdit = this.sut.canEditBook(bookId, username);
		Assertions.assertThat(canEdit).isTrue();
	}

	@Test
	void adminCanEditOthersVerifiedBook(){
		String username = "admin1";
		int bookId = 1;
		Boolean canEdit = this.sut.canEditBook(bookId, username);
		Assertions.assertThat(canEdit).isTrue();
	}

}
