
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsEmptyCollection;
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
import org.springframework.samples.petclinic.model.Review;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.WishedBook;
import org.springframework.samples.petclinic.repository.BookRepository;
import org.springframework.samples.petclinic.service.AuthoritiesService;
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
import org.springframework.samples.petclinic.service.exceptions.DuplicatedISBNException;
import org.springframework.samples.petclinic.service.exceptions.ReadOrWishedBookException;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doThrow;


@WebMvcTest(controllers = BookController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class BookControllerTests {

	private static final int		TEST_BOOK_ID	= 1;

	private static final int		TEST_BOOK_ID_2	= 2;

	@MockBean
	private UserService				userservice;

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
	private AuthoritiesService	authoritiesService;

	@MockBean
	private BookRepository	bookRepo;

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
    	genre.setId(3);
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
        when(this.bookService.findGenre()).thenReturn(Lists.newArrayList(genre));
		BDDMockito.given(this.userservice.findUserByUsername(user.getUsername())).willReturn(this.user);
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

		Mockito.when(this.bookService.findBookById(BookControllerTests.TEST_BOOK_ID_2)).thenReturn(book1);
		Mockito.when(this.bookService.findBookByTitleAuthorGenreISBN("")).thenReturn(booksNews1);
		Mockito.when(this.bookService.findBookByTitleAuthorGenreISBN("9788497593793")).thenReturn(booksNews2);

		List<Review> rev = new ArrayList<>();
		rev.add(new Review());
		rev.add(new Review());
		Mockito.when(this.reviewService.getReviewsFromBook(BookControllerTests.TEST_BOOK_ID)).thenReturn(rev);
	}

	@WithMockUser(value = "spring")
	@Test
	void testDelete() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/books/delete/{bookId}", BookControllerTests.TEST_BOOK_ID)).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/books"));

	}
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateBookForm() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/{bookId}/updateForm", BookControllerTests.TEST_BOOK_ID)).andExpect(status().isOk()).andExpect(model().attributeExists("book"))
			.andExpect(model().attribute("book", Matchers.hasProperty("title", Matchers.is("The name of the Wind"))))
			.andExpect(model().attribute("book", Matchers.hasProperty("image", Matchers.is("https://www.nombreviento.com/"))))
			.andExpect(model().attribute("book", Matchers.hasProperty("verified", Matchers.is(false)))).andExpect(model().attribute("book", Matchers.hasProperty("genre", Matchers.is(this.genre))))
			.andExpect(model().attributeExists("genres")).andExpect(view().name("books/UpdateBookForm"));

	}

	
	@WithMockUser(value = "spring2")
	@Test
	void testInitUpdateBookFormWithOtherUser() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/{bookId}/updateForm", BookControllerTests.TEST_BOOK_ID)).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/oups"));

	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateBookFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/books/update/{bookId}", BookControllerTests.TEST_BOOK_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "Change").param("author", "NewAuthor").param("editorial", "NewEdit")
				.param("genre.name", "fiction").param("ISBN", "9780345805362").param("pages", "12").param("synopsis", "nuevaSinopsis").param("image", "https://www.nombrevasiento.com/").param("publicationDate", "2018/11/11"))
			.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/books/" + BookControllerTests.TEST_BOOK_ID));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateBookFormWithDuplicatedISBN() throws Exception {
		doThrow(DuplicatedISBNException.class).when(bookService).save(any(Book.class));
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/books/update/{bookId}", BookControllerTests.TEST_BOOK_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "Change").param("author", "NewAuthor").param("editorial", "NewEdit")
				.param("genre.name", "fiction").param("ISBN", "9780345805362").param("pages", "12").param("synopsis", "nuevaSinopsis").param("image", "https://www.nombrevasiento.com/").param("publicationDate", "2018/11/11"))
			.andExpect(status().isOk()).andExpect(view().name("books/UpdateBookForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateBookFormWithErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/books/update/{bookId}", BookControllerTests.TEST_BOOK_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "").param("author", "NewAuthor").param("editorial", "NewEdit")
				.param("genre.name", "fiction").param("ISBN", "9780345805362").param("pages", "12").param("synopsis", "nuevaSinopsis").param("image", "https://www.nombrevasiento.com/").param("publicationDate", "2018/11/11"))
			.andExpect(status().isOk()).andExpect(view().name("/books/UpdateBookForm"));
	}
	@WithMockUser(value = "spring")
	@Test
	void testShowReadBooksListHtml() throws Exception {
		List<Integer> ids = new ArrayList<>();
		ids.add(BookControllerTests.TEST_BOOK_ID);
		Mockito.when(this.readBookService.findBooksIdByUser("spring")).thenReturn(ids);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/readBooks")).andExpect(status().isOk()).andExpect(model().attributeExists("selections"))
			.andExpect(view().name("books/booksList"));
	}
	@WithMockUser(value = "spring")
	@Test
	void testAddReadBook() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/books/readBooks/{bookId}", BookControllerTests.TEST_BOOK_ID).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/books"));
	}
	@WithMockUser(value = "spring")
	@Test
	void testShowRecomendationsListWithReadBooksHtml() throws Exception {
		List<Integer> ids = new ArrayList<>();
		ids.add(BookControllerTests.TEST_BOOK_ID);
		Mockito.when(this.readBookService.findBooksIdByUser("spring")).thenReturn(ids);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/recomendations")).andExpect(status().isOk()).andExpect(model().attributeExists("selections"))
			.andExpect(model().attribute("notEmpty", Matchers.is(true))).andExpect(view().name("books/recomendationList"));
	}
	@WithMockUser(value = "spring")
	@Test
	void testShowRecomendationsListWithoutReadBooksHtml() throws Exception {

		Mockito.when(this.readBookService.findBooksIdByUser("spring")).thenReturn(new ArrayList<>());
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/recomendations")).andExpect(status().isOk()).andExpect(model().attribute("emptyy", Matchers.is(true)))
			.andExpect(view().name("books/recomendationList"));
	}



	
	@WithMockUser(value = "spring")
	@Test
	void testInitFindForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/find")).andExpect(status().isOk()).andExpect(model().attributeExists("book")).andExpect(view().name("books/findBooks"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books")).andExpect(status().isOk()).andExpect(model().attributeExists("selections")).andExpect(view().name("books/booksList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessFindForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books").param("title", "9788497593793")).andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/books/" + BookControllerTests.TEST_BOOK_ID));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormNoBooksFound() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books").param("title", "Unknown Title")).andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors("book", "title"))
			.andExpect(model().attributeHasFieldErrorCode("book", "title", "notFound")).andExpect(view().name("books/findBooks"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowBook() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/{bookId}", BookControllerTests.TEST_BOOK_ID_2)).andExpect(status().isOk())
			.andExpect(model().attribute("book", Matchers.hasProperty("title", Matchers.is("Title test")))).andExpect(model().attribute("book", Matchers.hasProperty("author", Matchers.is("Author test"))))
			.andExpect(model().attribute("book", Matchers.hasProperty("ISBN", Matchers.is("9788497593793")))).andExpect(view().name("books/bookDetails"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessFindTopReadBooks() throws Exception {
		mockMvc.perform(get("/books/topRead")).andExpect(status().isOk()).andExpect(model().attributeExists("selections"))
				.andExpect(view().name("books/booksList"));
	}
	@WithMockUser(value = "spring")
	@Test
	void testProcessListWishedBooks() throws Exception {
		mockMvc.perform(get("/books/wishList")).andExpect(status().isOk()).andExpect(model().attributeExists("selections"))
				.andExpect(view().name("books/booksList"));
	}
	@WithMockUser(value = "spring")
	@Test
	void testProcessAddWishedBooks() throws Exception {
		mockMvc.perform(post("/books/wishList/{book.id}",TEST_BOOK_ID)
				.with(csrf())).andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/books"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNotAddWishedBook() throws Exception {
		doThrow(ReadOrWishedBookException.class).when(wishedBookService).save(any(WishedBook.class));

		when(this.wishedBookService.esWishedBook(TEST_BOOK_ID)).thenReturn(true);
		mockMvc.perform(post("/books/wishList/{book.id}",TEST_BOOK_ID)
				.with(csrf())).andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessFindEmptyTopReadBooks() throws Exception {
		when(this.readBookService.topReadBooks()).thenReturn(Lists.newArrayList());
		mockMvc.perform(get("/books/topRead")).andExpect(status().isOk()).andExpect(model().attribute("selections", IsEmptyCollection.empty()))
				.andExpect(view().name("books/booksList"));
	}

	@WithMockUser(value = "spring",authorities= {"admin"})
	@Test
	void testVerifyBookAdmin() throws Exception {
		mockMvc.perform(get("/admin/books/{bookId}/verify",TEST_BOOK_ID)
				)
	.andExpect(status().is3xxRedirection())
	.andExpect(view().name("redirect:/books/" + TEST_BOOK_ID));
	}

	@WithMockUser(value = "spring") 
	@Test 
	   void testAddBook() throws Exception { 
		   mockMvc.perform(get("/books/add")).andExpect(status().isOk()) 
				   .andExpect(view().name("books/bookAdd")).andExpect(model().attributeExists("book")); 
	   } 

	@WithMockUser(value = "spring") 
	@Test 
   void testSaveBook() throws Exception { 
	   mockMvc.perform(post("/books/save") 
						   .with(csrf()) 
						   .param("title", "El nombre del viento") 
						   .param("author", "Patrick") 
						   .param("editorial", "SM") 
						   .param("genre.name", "Novela") 
						   .param("ISBN", "9780345805362") 
						   .param("pages", "100") 
						   .param("synopsis","He robado princesas a reyes agónicos. Incendié la ciudad de Trebon. He pasado la noche con Felurian y he despertado vivo y cuerdo. Me expulsaron de la Universidad a una edad a la que a la mayoría todavía no los dejan entrar. He recorrido de noche caminos de los que otros no se atreven a hablar ni siquiera de día. He hablado con dioses, he amado a mujeres y he escrito canciones que hacen llorar a los bardos. Me llamo Kvothe. Quizá hayas oído hablar de mí\"") 
						   .param("publicationDate","2015/12/11") 
						   .param("image","https://pictures.abebooks.com/isbn/9780575081406-es.jpg"))							 
						   .andExpect(status().is3xxRedirection()) 
						   .andExpect(redirectedUrl("/books")); 
   } 

	@WithMockUser(value = "spring") 
	@Test 
   void testSaveBookHasErrors() throws Exception { 
	   mockMvc.perform(post("/books/save") 
						   .with(csrf()) 
						   .param("title", " ") 
						   .param("author", " ") 
						   .param("editorial", " ") 
						   .param("genre", " ") 
						   .param("ISBN", " ") 
						   .param("pages", "1") 
						   .param("synopsis"," ") 
						   .param("publicationDate","2015/11/12") 
						   .param("image",""))	 
						   .andExpect(status().isOk()) 
						   .andExpect(model().attributeHasErrors("book")) 
						   .andExpect(model().attributeHasFieldErrors("book","title")) 
						   .andExpect(model().attributeHasFieldErrors("book","author")) 
						   .andExpect(model().attributeHasFieldErrors("book","editorial")) 
						   .andExpect(model().attributeHasFieldErrors("book","genre")) 
						   .andExpect(model().attributeHasFieldErrors("book","ISBN")) 
						   .andExpect(model().attributeHasFieldErrors("book","synopsis")) 
						   .andExpect(model().attributeHasFieldErrors("book","image")) 
						   .andExpect(view().name("books/bookAdd")); 
   } 

   @WithMockUser(value = "spring") 
	@Test 
   void testSaveBookHasDuplicatedISBN() throws Exception { 
	   doThrow(DuplicatedISBNException.class).when(bookService).save(any(Book.class));
	   mockMvc.perform(post("/books/save") 
						   .with(csrf()) 
						   .param("title", "El nombre del viento") 
						   .param("author", "Patrick") 
						   .param("editorial", "SM") 
						   .param("genre.name", "Novela") 
						   .param("ISBN", "9780345805362") 
						   .param("pages", "100") 
						   .param("synopsis","He robado princesas a reyes agónicos. Incendié la ciudad de Trebon. He pasado la noche con Felurian y he despertado vivo y cuerdo. Me expulsaron de la Universidad a una edad a la que a la mayoría todavía no los dejan entrar. He recorrido de noche caminos de los que otros no se atreven a hablar ni siquiera de día. He hablado con dioses, he amado a mujeres y he escrito canciones que hacen llorar a los bardos. Me llamo Kvothe. Quizá hayas oído hablar de mí\"") 
						   .param("publicationDate","2015/12/11") 
						   .param("image","https://pictures.abebooks.com/isbn/9780575081406-es.jpg"))							 
						   .andExpect(status().isOk()) 
						   .andExpect(view().name("books/bookAdd")); 
   }
}
