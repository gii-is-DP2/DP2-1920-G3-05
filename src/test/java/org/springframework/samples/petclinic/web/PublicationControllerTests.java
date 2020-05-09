package org.springframework.samples.petclinic.web;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Publication;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.samples.petclinic.service.PublicationService;
import org.springframework.samples.petclinic.service.ReadBookService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers=PublicationController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
class PublicationControllerTests {
	
	private static final int TEST_BOOK_ID = 1;
	private static final int TEST_BOOK_ID2 = 2;
	private static final int TEST_PUBLICATION_ID = 1;
	private static final int TEST_PUBLICATION_ID2 = 2;
	@Autowired
	private MockMvc		mockMvc;
	
	private User		user;
		
	@MockBean
	private PublicationService publicationService;

	@MockBean
	private UserService			userService;

	@MockBean
	private BookService 		bookService;

	@MockBean
	private ReadBookService		readBookService;
	
	@MockBean
	private AuthoritiesService	authoritiesService;
	
	

	@BeforeEach
	void setup() {
		User user = new User();
		user.setEnabled(true);
		user.setUsername("Owner2");
		user.setPassword("0wn3r");
		Book book = new Book();
        book.setId(TEST_BOOK_ID);
        
        Book book2 = new Book();
        book2.setId(TEST_BOOK_ID2);
        
        Publication publication3 = new Publication();
        publication3.setTitle("title");
        publication3.setDescription("description");
        publication3.setImage("https://www.nombreviento.com/");
        publication3.setBook(book);
        publication3.setUser(user);
        publication3.setPublicationDate(LocalDate.of(2020, 01, 12));
        publication3.setId(TEST_PUBLICATION_ID);
        
        Publication publication4 = new Publication();
        publication4.setTitle("title2");
        publication4.setDescription("description2");
        publication4.setImage("https://www.nombreviento2.com/");
        publication4.setBook(book2);
        publication4.setUser(user);
        publication4.setPublicationDate(LocalDate.of(2020, 02, 12));
        publication4.setId(TEST_PUBLICATION_ID2);
        
        when(publicationService.findById(TEST_PUBLICATION_ID)).thenReturn(publication3);
        when(publicationService.findById(TEST_PUBLICATION_ID2)).thenReturn(publication4);
        when(publicationService.publicationMioOAdmin(TEST_PUBLICATION_ID, "spring")).thenReturn(true);
        when(readBookService.esReadBook(TEST_BOOK_ID, "spring")).thenReturn(true);
        when(readBookService.esReadBook(TEST_BOOK_ID2, "spring")).thenReturn(false);
        when(bookService.findBookById(TEST_BOOK_ID)).thenReturn(book);
        when(publicationService.publicationMio(TEST_PUBLICATION_ID, "spring")).thenReturn(true);
        when(publicationService.publicationMio(TEST_PUBLICATION_ID2, "spring")).thenReturn(false);
		BDDMockito.given(this.userService.findUserByUsername(user.getUsername())).willReturn(this.user);
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testListPublication() throws Exception {
		List<Publication> publications = new ArrayList<Publication>();
		publications.add(new Publication());
		publications.add(new Publication());
		when(this.publicationService.findAllPublicationFromBook(TEST_BOOK_ID)).thenReturn(publications);
		mockMvc.perform(get("/books/{bookId}/publications", TEST_BOOK_ID)).andExpect(model().attributeExists("selections"))
		.andExpect(status().isOk())
		.andExpect(view().name("publications/publicationList"));
	}

	
	@WithMockUser(value = "spring")
	@Test
	void testShowPublication() throws Exception {
		User user = new User();
		user.setEnabled(true);
		user.setUsername("Owner2");
		user.setPassword("0wn3r");
        mockMvc.perform(get("/publications/{publicationId}", TEST_PUBLICATION_ID))
		.andExpect(status().isOk())
		.andExpect(model().attribute("publication", hasProperty("title", is("title"))))
		.andExpect(model().attribute("publication", hasProperty("description", is("description"))))
		.andExpect(model().attribute("publication", hasProperty("image", is("https://www.nombreviento.com/"))))
		.andExpect(model().attribute("publication", hasProperty("publicationDate", is(LocalDate.of(2020, 01, 12)))))
		.andExpect(model().attribute("publication", hasProperty("user", is(user))))
		.andExpect(model().attributeExists("propiedad"))
		.andExpect(model().attributeExists("propiedad2"))
		.andExpect(view().name("publications/publicationDetails"));
	}
	
	 @WithMockUser(value = "spring")
	  @Test
	  void testAddPublication() throws Exception {
		 mockMvc.perform(get("/books/{bookId}/publications/publicationAdd", TEST_BOOK_ID))
		 .andExpect(status().isOk())
		 .andExpect(model().attributeExists("publication"))
		 .andExpect(view().name("publications/publicationAdd"));
	 }

	 @WithMockUser(value = "spring")
	  @Test
	  void testAddPublicationNoReadBook() throws Exception {
		 mockMvc.perform(get("/books/{bookId}/publications/publicationAdd", TEST_BOOK_ID2))
		 .andExpect(status().is3xxRedirection())
		 .andExpect(view().name("redirect:/oups"));
	 }
	 
	 @WithMockUser(value = "spring")
	 @Test
	 void testSavePublication() throws Exception{
		 mockMvc.perform(post("/books/{bookId}/publications/save", TEST_BOOK_ID)
				 .with(csrf())
				 .param("title", "Test title")
				 .param("description", "test description")
				 .param("image", "https://www.nombreviento.com/"))
		 .andExpect(status().is3xxRedirection())
		 .andExpect(view().name("redirect:/books/"+ TEST_BOOK_ID));
	 }
	 
	 @WithMockUser(value = "spring")
	 @Test
	 void testSavePublicationNoReadBook() throws Exception{
		 mockMvc.perform(post("/books/{bookId}/publications/save", TEST_BOOK_ID2)
				 .with(csrf())
				 .param("title", "Test title")
				 .param("description", "test description")
				 .param("image", "https://www.nombreviento.com/"))
		 .andExpect(status().is3xxRedirection())
		 .andExpect(view().name("redirect:/oups"));
	 }
	 
	 @WithMockUser(value = "spring")
	 @Test
	 void testSavePublicationWithErrors() throws Exception{
		 mockMvc.perform(post("/books/{bookId}/publications/save", TEST_BOOK_ID)
				 .with(csrf())
				 .param("title", " ")
				 .param("description", " ")
				 .param("image", " "))
		 .andExpect(status().isOk())
		 .andExpect(model().attributeHasErrors("publication"))
         .andExpect(model().attributeHasFieldErrors("publication", "title"))
         .andExpect(model().attributeHasFieldErrors("publication", "description"))
         .andExpect(model().attributeHasFieldErrors("publication", "image"))
		 .andExpect(view().name("publications/publicationAdd"));
	 }
	 
	 @WithMockUser(value = "spring")
	    @Test
	    void testFormEditMyReview() throws Exception{
	        mockMvc.perform(get("/publications/{publicationId}/updateForm", TEST_PUBLICATION_ID))
	                .andExpect(status().isOk())
	                .andExpect(model().attributeExists("publication"))
	                .andExpect(model().attribute("publication", hasProperty("title", is("title"))))
	                .andExpect(model().attribute("publication", hasProperty("description", is("description"))))
	                .andExpect(model().attribute("publication", hasProperty("image", is("https://www.nombreviento.com/"))))
	                .andExpect(view().name("publications/UpdatePublicationForm"));
	    }
	 
	 @WithMockUser(value = "spring")
	    @Test
	    void testFormEditOtherReview() throws Exception{
	        mockMvc.perform(get("/publications/{publicationId}/updateForm", TEST_PUBLICATION_ID2))
	        .andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/oups"));    
	    }

	 @WithMockUser(value = "spring")
	 @Test
	 void testUpdateMyPublication() throws Exception{
		 mockMvc.perform(post("/publications/update/{publicationId}", TEST_PUBLICATION_ID)
				 .with(csrf())
				 .param("title", "Edited title")
				 .param("description", "prueba")
				 .param("image", "https://www.google.com/"))
		 		.andExpect(status().is3xxRedirection())
		 		.andExpect(view().name("redirect:/publications/" + TEST_PUBLICATION_ID));  
	 }  

	 @WithMockUser(value = "spring")
	 @Test
	 void testUpdateMyPublicationWithErrors() throws Exception{
		 mockMvc.perform(post("/publications/update/{publicationId}", TEST_PUBLICATION_ID)
				 .with(csrf())
				 .param("title", " ")
				 .param("description", "")
				 .param("image", ""))
		  		.andExpect(status().isOk())
		  		.andExpect(model().attributeHasErrors("publication"))
		  		.andExpect(model().attributeHasFieldErrors("publication", "title"))
		  		.andExpect(model().attributeHasFieldErrors("publication", "description"))
		  		.andExpect(model().attributeHasFieldErrors("publication", "image"))
		  		.andExpect(view().name("publications/UpdatePublicationForm"));
	 }  
	 
	 @WithMockUser(value = "spring")
	 @Test
	 void testUpdateOthersPublication() throws Exception{
		 mockMvc.perform(post("/publications/update/{publicationId}", TEST_PUBLICATION_ID2)
				 .with(csrf())
				 .param("title", "Edited title")
				 .param("description", "prueba")
				 .param("image", "https://www.google.com/"))
		 		.andExpect(status().is3xxRedirection())
		 		.andExpect(view().name("redirect:/oups"));
	 }  
	 
	@WithMockUser(value = "spring")
	@Test
	void testDeletePublication() throws Exception{
	    mockMvc.perform(get("/books/{bookId}/delete/{publicationId}", TEST_BOOK_ID, TEST_PUBLICATION_ID))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/books/" + TEST_BOOK_ID));
	}

	@WithMockUser(value = "spring")
	@Test
	void testDeleteOthersPublication() throws Exception{
	    mockMvc.perform(get("/books/{bookId}/delete/{publicationId}", TEST_BOOK_ID, TEST_PUBLICATION_ID2))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/oups"));
	}
	 
}
