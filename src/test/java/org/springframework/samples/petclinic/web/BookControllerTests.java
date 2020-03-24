
package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Review;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.samples.petclinic.service.ReadBookService;
import org.springframework.samples.petclinic.service.ReviewService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.WishedBookService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = BookController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)

public class BookControllerTests {

	private static final int	TEST_BOOK_ID	= 1;

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private BookService			bookService;

	@MockBean
	private UserService			userService;

	@MockBean
	private ReadBookService		readBookService;

	@MockBean
	private ReviewService		reviewService;

	@MockBean
	private WishedBookService	wishedBookService;


	@BeforeEach
	void setup() {

		List<Book> booksNews1 = new ArrayList<Book>();
		booksNews1.add(new Book());
		booksNews1.add(new Book());
		booksNews1.add(new Book());

		List<Book> booksNews2 = new ArrayList<Book>();
		Book book1 = new Book();
		book1.setId(BookControllerTests.TEST_BOOK_ID);
		book1.setTitle("Title test");
		book1.setAuthor("Author test");
		book1.setISBN("9788497593793");
		booksNews2.add(book1);

		Mockito.when(this.bookService.findBookById(BookControllerTests.TEST_BOOK_ID)).thenReturn(book1);
		Mockito.when(this.bookService.findBookByTitleAuthorGenreISBN("")).thenReturn(booksNews1);
		Mockito.when(this.bookService.findBookByTitleAuthorGenreISBN("9788497593793")).thenReturn(booksNews2);

		List<Review> rev = new ArrayList<>();
		rev.add(new Review());
		rev.add(new Review());
		Mockito.when(this.reviewService.getReviewsFromBook(BookControllerTests.TEST_BOOK_ID)).thenReturn(rev);

	}

	@WithMockUser(value = "spring")
	@Test
	void testInitFindForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/find")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("book")).andExpect(MockMvcResultMatchers.view().name("books/findBooks"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("selections")).andExpect(MockMvcResultMatchers.view().name("books/booksList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessFindForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books").param("title", "9788497593793")).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/books/" + BookControllerTests.TEST_BOOK_ID));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormNoBooksFound() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books").param("title", "Unknown Title")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("book", "title"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("book", "title", "notFound")).andExpect(MockMvcResultMatchers.view().name("books/findBooks"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowBook() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/{bookId}", BookControllerTests.TEST_BOOK_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("book", Matchers.hasProperty("title", Matchers.is("Title test")))).andExpect(MockMvcResultMatchers.model().attribute("book", Matchers.hasProperty("author", Matchers.is("Author test"))))
			.andExpect(MockMvcResultMatchers.model().attribute("book", Matchers.hasProperty("ISBN", Matchers.is("9788497593793")))).andExpect(MockMvcResultMatchers.view().name("books/bookDetails"));
	}

}
