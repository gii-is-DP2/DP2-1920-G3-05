package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.Collection;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.BDDMockito.BDDMyOngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Genre;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
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


@WebMvcTest(controllers=BookController.class,
includeFilters = @ComponentScan.Filter(value = GenreFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
class BookControllerTests {

	private static final int TEST_BOOK_ID = 1;
	@Autowired
	private BookController		BookController;

	@MockBean
	private BookService			bookService;
	@MockBean
	private ReadBookService		readBookService;
	@MockBean
	private WishedBookService	wishedBookService;
	@MockBean
	private ReviewService		reviewService;
	@MockBean
	private UserService			userService;

	@MockBean
	private AuthoritiesService	authoritiesService;

	@Autowired
	private MockMvc				mockMvc;
	private Book				book;
	private Genre				genre;
	private User				user;
	

	@BeforeEach
	void setup() {
		User user = new User();
		user.setEnabled(true);
		user.setUsername("Owner2");
		user.setPassword("0wn3r");
		Genre genre = new Genre();
		genre.setId(3);
		genre.setName("Novela");
		given(this.bookService.findGenre()).willReturn(Lists.newArrayList(genre));
		BDDMockito.given(this.userService.findUserByUsername(user.getUsername())).willReturn(this.user);
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
							.param("genre", "Novela")
							.param("ISBN", "9780345805362")
							.param("pages", "100")
							.param("synopsis","He robado princesas a reyes agónicos. Incendié la ciudad de Trebon. He pasado la noche con Felurian y he despertado vivo y cuerdo. Me expulsaron de la Universidad a una edad a la que a la mayoría todavía no los dejan entrar. He recorrido de noche caminos de los que otros no se atreven a hablar ni siquiera de día. He hablado con dioses, he amado a mujeres y he escrito canciones que hacen llorar a los bardos. Me llamo Kvothe. Quizá hayas oído hablar de mí\"")
							.param("publicationDate","2015/12/11")
							.param("image","https://pictures.abebooks.com/isbn/9780575081406-es.jpg"))							
							.andExpect(status().is3xxRedirection())
							.andExpect(view().name("redirect:/books"));
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


	

	}
	

