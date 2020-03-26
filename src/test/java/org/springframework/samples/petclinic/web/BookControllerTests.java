package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.ReadBookService;
import org.springframework.samples.petclinic.service.ReviewService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.WishedBookService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = BookController.class, includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE), excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))
public class BookControllerTests {

	
	private final int TEST_BOOK_ID=3;
	@Autowired
	private BookController bookController;
	@MockBean
	private BookService bookService;
	@MockBean
	private UserService userService;

	@MockBean
	private AuthoritiesService authoritiesService;
	@MockBean
	private ReadBookService readBookService;
	@MockBean
	private WishedBookService wishedBookService;
	@MockBean
	private ReviewService reviewService;
	@MockBean
	private PetService petService;
	@Autowired
	private MockMvc mockMvc;
	@BeforeEach
	void setup() {
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext= Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
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
		given(this.wishedBookService.esWishedBook(TEST_BOOK_ID)).willReturn(true);
		mockMvc.perform(post("/books/wishList/{book.id}",TEST_BOOK_ID)
				.with(csrf())).andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/oups"));
	}
	@WithMockUser(value = "spring")
	@Test
	void testProcessNotAddReadBook() throws Exception {
		given(this.readBookService.esReadBook(TEST_BOOK_ID, "spring")).willReturn(true);
		mockMvc.perform(post("/books/wishList/{book.id}",TEST_BOOK_ID)
				.with(csrf())).andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/oups"));
	}
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindEmptyTopReadBooks() throws Exception {
		given(this.readBookService.topReadBooks()).willReturn(Lists.newArrayList());
		mockMvc.perform(get("/books/topRead")).andExpect(status().isOk()).andExpect(model().attribute("selections", IsEmptyCollection.empty()))
				.andExpect(view().name("books/booksList"));
	}

	@WithMockUser(value = "spring",authorities= {"admin"})
	@Test
	void testVerifyBookAdmin() throws Exception {
		mockMvc.perform(get("/admin/books/{bookId}/verify",TEST_BOOK_ID)
				)
	.andExpect(status().is3xxRedirection())
	.andExpect(view().name("redirect:/books/3"));
	}
}
