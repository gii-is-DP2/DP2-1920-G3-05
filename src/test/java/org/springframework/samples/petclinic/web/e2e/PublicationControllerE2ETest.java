package org.springframework.samples.petclinic.web.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import java.time.LocalDate;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
  webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
 class PublicationControllerE2ETest {

    private static final int TEST_BOOK_ID = 1;

    private static final int TEST_PUBLICATION_ID = 1;

    private static final int TEST_BOOK_ID_2 = 2;

    private static final int TEST_PUBLICATION_ID_2 = 2;

    private static final int TEST_PUBLICATION_ID_3 = 3;

    @Autowired
    private MockMvc mockMvc;
    
    @WithMockUser(username = "admin1", authorities = {"admin"})
	  @Test
	  void testListPublication() throws Exception {
		mockMvc.perform(get("/books/{bookId}/publications", TEST_BOOK_ID)).andExpect(model().attributeExists("selections"))
		.andExpect(status().isOk())
		.andExpect(view().name("publications/publicationList"));
    }
    
    @WithMockUser(username = "admin1", authorities = {"admin"})
	  @Test
  	void testShowPublication() throws Exception {
        mockMvc.perform(get("/publications/{publicationId}", TEST_PUBLICATION_ID))
		           .andExpect(status().isOk())
		           .andExpect(model().attribute("publication", hasProperty("title", is("publication 1"))))
		           .andExpect(model().attribute("publication", hasProperty("description", is("this is tests data"))))
		           .andExpect(model().attribute("publication", hasProperty("image", is("https://lh3.googleusercontent.com/proxy/9xJwN4k_Q-pPsRiDF6biPeUar08kxIY9qEKMk9k2oOF_JHMly-x4fA0JuXPpS7WR-bJBCiSlfaRQ97ohxkQvU4X2gQMFOS16W1zdoX4Tg7Bl4APN4ObQtlGjaYwbavENT07Uql5UrHK9VnviQAP_OxNVYh0"))))
		           .andExpect(model().attribute("publication", hasProperty("publicationDate", is(LocalDate.of(2020,03,07)))))
		           .andExpect(model().attribute("publication", hasProperty("user", hasProperty("username", is("admin1")))))
		           .andExpect(model().attributeExists("propiedad"))
		           .andExpect(model().attributeExists("propiedad2"))
		           .andExpect(view().name("publications/publicationDetails"));
    }
    
    @WithMockUser(username = "admin1", authorities = {"admin"})
    @Test
    void testAddPublication() throws Exception {
       mockMvc.perform(get("/books/{bookId}/publications/publicationAdd", TEST_BOOK_ID))
       .andExpect(status().isOk())
       .andExpect(model().attributeExists("publication"))
       .andExpect(view().name("publications/publicationAdd"));
   }

    @WithMockUser(username = "admin1", authorities = {"admin"})
    @Test
    void testAddPublicationNoReadBook() throws Exception {
      mockMvc.perform(get("/books/{bookId}/publications/publicationAdd", TEST_BOOK_ID_2))
      .andExpect(status().is3xxRedirection())
      .andExpect(view().name("redirect:/oups"));
    }

    @WithMockUser(username = "admin1", authorities = {"admin"})
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

    @WithMockUser(username = "admin1", authorities = {"admin"})
    @Test
    void testSavePublicationNoReadBook() throws Exception{
      mockMvc.perform(post("/books/{bookId}/publications/save", TEST_BOOK_ID_2)
              .with(csrf())
              .param("title", "Test title")
              .param("description", "test description")
              .param("image", "https://www.nombreviento.com/"))
      .andExpect(status().is3xxRedirection())
      .andExpect(view().name("redirect:/oups"));
    }

    @WithMockUser(username = "admin1", authorities = {"admin"})
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

    @WithMockUser(username = "admin1", authorities = {"admin"})
    @Test
	void testFormEditMyReview() throws Exception{
	    mockMvc.perform(get("/publications/{publicationId}/updateForm", TEST_PUBLICATION_ID))
	                .andExpect(status().isOk())
	                .andExpect(model().attributeExists("publication"))
                    .andExpect(model().attribute("publication", hasProperty("title", is("publication 1"))))
                    .andExpect(model().attribute("publication", hasProperty("description", is("this is tests data"))))
                    .andExpect(model().attribute("publication", hasProperty("image", is("https://lh3.googleusercontent.com/proxy/9xJwN4k_Q-pPsRiDF6biPeUar08kxIY9qEKMk9k2oOF_JHMly-x4fA0JuXPpS7WR-bJBCiSlfaRQ97ohxkQvU4X2gQMFOS16W1zdoX4Tg7Bl4APN4ObQtlGjaYwbavENT07Uql5UrHK9VnviQAP_OxNVYh0"))))
	                .andExpect(view().name("publications/UpdatePublicationForm"));
    }

    @WithMockUser(username = "admin1", authorities = {"admin"})
    @Test
    void testFormEditOtherReview() throws Exception{
        mockMvc.perform(get("/publications/{publicationId}/updateForm", TEST_PUBLICATION_ID_2))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/oups"));    
    }

    @WithMockUser(username = "admin1", authorities = {"admin"})
    @Test
    void testUpdateMyPublication() throws Exception{
        mockMvc.perform(post("/publications/update/{publicationId}", TEST_PUBLICATION_ID_3)
                .with(csrf())
                .param("title", "Edited title")
                .param("description", "prueba")
                .param("image", "https://www.google.com/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/publications/" + TEST_PUBLICATION_ID_3));  
    } 

    @WithMockUser(username = "admin1", authorities = {"admin"})
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
    
    @WithMockUser(username = "admin1", authorities = {"admin"})
    @Test
    void testUpdateOthersPublication() throws Exception{
        mockMvc.perform(post("/publications/update/{publicationId}", TEST_PUBLICATION_ID_2)
                .with(csrf())
                .param("title", "Edited title")
                .param("description", "prueba")
                .param("image", "https://www.google.com/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/oups"));
    }

    @WithMockUser(username = "admin1", authorities = {"admin"})
    @Test
    void testDeletePublication() throws Exception{
        mockMvc.perform(get("/books/{bookId}/delete/{publicationId}", TEST_BOOK_ID, TEST_PUBLICATION_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/" + TEST_BOOK_ID));
    }

    @WithMockUser(username = "reader1", authorities = {"reader"})
	@Test
	void testDeleteOthersPublication() throws Exception{
	    mockMvc.perform(get("/books/{bookId}/delete/{publicationId}", TEST_BOOK_ID, TEST_PUBLICATION_ID_2))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/oups"));
	}
    
}