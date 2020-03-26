
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Genre;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.BookInNewService;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.samples.petclinic.service.MeetingAssistantService;
import org.springframework.samples.petclinic.service.MeetingService;
import org.springframework.samples.petclinic.service.NewService;
import org.springframework.samples.petclinic.service.PublicationService;
import org.springframework.samples.petclinic.service.ReadBookService;
import org.springframework.samples.petclinic.service.ReviewService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.WishedBookService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = BookController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class BookControllerTests {

	private static final int		TEST_BOOK_ID	= 1;

	@MockBean
	private UserService				userservice;

	@Autowired
	private BookController			bookController;

	@Autowired
	private MockMvc					mockMvc;

	@MockBean
	private BookService				bookService;

	@MockBean
	private ReviewService			reviewService;

	@MockBean
	private MeetingService			meetingService;

	@MockBean
	private MeetingAssistantService	meetingAssistantService;

	@MockBean
	private NewService				newService;

	@MockBean
	private BookInNewService		bookNewService;

	@MockBean
	private PublicationService		publicationService;

	@MockBean
	private ReadBookService			readBookService;

	@MockBean
	private WishedBookService		wishedBookService;
	private Book					book;

	private Genre					genre;
	private User					user;


	@BeforeEach
	void setup() {
		this.book = new Book();
		this.genre = new Genre();
		this.user = new User();
		this.user.setEnabled(true);
		this.user.setUsername("Owner2");
		this.user.setPassword("0wn3r");

		this.genre.setName("Novela");
		this.book.setId(BookControllerTests.TEST_BOOK_ID);
		this.book.setAuthor("Patrick Rothfuss");
		this.book.setEditorial("DAW Books");
		this.book.setGenre(this.genre);
		this.book.setImage("https://www.nombreviento.com/");
		this.book.setISBN("9780345805362");
		LocalDate publication = LocalDate.of(2007, 3, 27);
		this.book.setPublicationDate(publication);
		this.book.setSynopsis(
			"La obra se desarrolla en un mundo fantástico y narra la historia de Kvothe (pronunciado “Cuouz”),3​ arcanista, asesino, enamorado, músico, estudiante y aventurero; y de cómo se convirtió en un personaje legendario. Usando el nombre de Kote para ocultar su verdadera identidad, regenta una apartada posada llamada Roca de Guía, acompañado de su discípulo Bast. Un día les visita Devan Lochees, un escribano conocido como “Cronista”, interesado en escribir las biografías de las figuras más importantes de su tiempo, quien intenta convencerle para que revele su verdadera historia. Kvothe accede, con la condición de hacerlo en tres días.");
		this.book.setTitle("The name of the Wind");
		this.book.setUser(this.user);
		this.book.setVerified(false);
		BDDMockito.given(this.bookService.findBookById(BookControllerTests.TEST_BOOK_ID)).willReturn(this.book);
		BDDMockito.given(this.bookService.canEditBook(BookControllerTests.TEST_BOOK_ID, "spring")).willReturn(true);
		Mockito.when(this.reviewService.getReviewsIdFromBook(BookControllerTests.TEST_BOOK_ID)).thenReturn(new ArrayList<Integer>());
		Mockito.when(this.meetingService.getMeetingsIdFromBook(BookControllerTests.TEST_BOOK_ID)).thenReturn(new ArrayList<Integer>());
		Mockito.when(this.newService.getNewsFromBook(BookControllerTests.TEST_BOOK_ID)).thenReturn(new ArrayList<Integer>());
		Mockito.when(this.publicationService.getPublicationsFromBook(BookControllerTests.TEST_BOOK_ID)).thenReturn(new ArrayList<Integer>());
		Mockito.when(this.publicationService.getPublicationsFromBook(BookControllerTests.TEST_BOOK_ID)).thenReturn(new ArrayList<Integer>());
		Mockito.doNothing().when(this.readBookService).deleteReadBookByBookId(BookControllerTests.TEST_BOOK_ID);
		Mockito.doNothing().when(this.wishedBookService).deleteByBookId(BookControllerTests.TEST_BOOK_ID);
		Mockito.doNothing().when(this.bookService).deleteById(BookControllerTests.TEST_BOOK_ID, "spring");

	}

	@WithMockUser(value = "spring")
	@Test
	void testDelete() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/books/delete/{bookId}", BookControllerTests.TEST_BOOK_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.redirectedUrl("/books"));

	}
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateBookForm() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/{bookId}/updateForm", BookControllerTests.TEST_BOOK_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("book"))
			.andExpect(MockMvcResultMatchers.model().attribute("book", Matchers.hasProperty("title", Matchers.is("The name of the Wind"))))
			.andExpect(MockMvcResultMatchers.model().attribute("book", Matchers.hasProperty("image", Matchers.is("https://www.nombreviento.com/"))))
			.andExpect(MockMvcResultMatchers.model().attribute("book", Matchers.hasProperty("verified", Matchers.is(false)))).andExpect(MockMvcResultMatchers.model().attribute("book", Matchers.hasProperty("genre", Matchers.is(this.genre))))
			.andExpect(MockMvcResultMatchers.model().attributeExists("genres")).andExpect(MockMvcResultMatchers.view().name("books/UpdateBookForm"));

	}
	@WithMockUser(value = "spring2")
	@Test
	void testInitUpdateBookFormWithOtherUser() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/{bookId}/updateForm", BookControllerTests.TEST_BOOK_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.redirectedUrl("/oups"));

	}
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateBookFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/books/update/{bookId}", BookControllerTests.TEST_BOOK_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "Change").param("author", "NewAuthor").param("editorial", "NewEdit")
				.param("genre.name", "fiction").param("ISBN", "9780345805362").param("pages", "12").param("synopsis", "nuevaSinopsis").param("image", "https://www.nombrevasiento.com/").param("publicationDate", "2018/11/11"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.redirectedUrl("/books/" + BookControllerTests.TEST_BOOK_ID));
	}
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateBookFormWithErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/books/update/{bookId}", BookControllerTests.TEST_BOOK_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "").param("author", "NewAuthor").param("editorial", "NewEdit")
				.param("genre.name", "fiction").param("ISBN", "9780345805362").param("pages", "12").param("synopsis", "nuevaSinopsis").param("image", "https://www.nombrevasiento.com/").param("publicationDate", "2018/11/11"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("/books/UpdateBookForm"));
	}
	@WithMockUser(value = "spring")
	@Test
	void testShowReadBooksListHtml() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/readBooks")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("selections"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recomendations")).andExpect(MockMvcResultMatchers.view().name("books/booksList"));
	}
	@WithMockUser(value = "spring")
	@Test
	void testAddReadBook() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/books/readBooks/{bookId}", BookControllerTests.TEST_BOOK_ID).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/books"));
	}

}
