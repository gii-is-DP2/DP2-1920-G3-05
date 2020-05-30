package org.springframework.samples.petclinic.web.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
  webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
 class ItApiControllerE2ETests {

    @Autowired
    private MockMvc     mockMvc;

	@WithMockUser(username="reader1",authorities= {"reader"})
    @Test
    void testSearchForm() throws Exception {
        this.mockMvc.perform(get("/itBooks/find"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("itBook"))
                    .andExpect(view().name("itBooks/find"));
    }
    
	@WithMockUser(username="reader1",authorities= {"reader"})
    @Test
    void testProcessFindForm() throws Exception {
        this.mockMvc.perform(get("/itBooks").param("title", "apache"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("itBooks"))
                    .andExpect(view().name("itBooks/list"));
    }

	@WithMockUser(username="reader1",authorities= {"reader"})
    @Test
    void testGetDetails() throws Exception {
        this.mockMvc.perform(get("/itBooks/details/9781484206485"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("itBook"))
                    .andExpect(model().attribute("itBook", hasProperty("title", is("Practical MongoDB"))))
                    .andExpect(model().attribute("itBook", hasProperty("subtitle", is("Architecting, Developing, and Administering MongoDB"))))
                    .andExpect(model().attribute("itBook", hasProperty("image", is("https://itbook.store/img/books/9781484206485.png"))))
                    .andExpect(model().attribute("itBook", hasProperty("isbn13", is("9781484206485"))))
                    .andExpect(model().attribute("itBook", hasProperty("isbn10", is("1484206487"))))
                    .andExpect(model().attribute("itBook", hasProperty("authors", is("Shakuntala Gupta Edward, Navin Sabharwal"))))
                    .andExpect(model().attribute("itBook", hasProperty("desc", is("Practical Guide to MongoDB: Architecting, Developing, and Administering MongoDB begins with a short introduction to the basics of NoSQL databases and then introduces readers to MongoDB - the leading document based NoSQL database, acquainting them step-by-step with all aspects of MongoDB.Practical Gu..."))))
                    .andExpect(model().attribute("itBook", hasProperty("language", is("English"))))
                    .andExpect(model().attribute("itBook", hasProperty("pages", is("272"))))
                    .andExpect(model().attribute("itBook", hasProperty("price", is("$35.20"))))
                    .andExpect(model().attribute("itBook", hasProperty("publisher", is("Apress"))))
                    .andExpect(model().attribute("itBook", hasProperty("rating", is("3"))))
                    .andExpect(model().attribute("itBook", hasProperty("url", is("https://itbook.store/books/9781484206485"))))
                    .andExpect(model().attribute("itBook", hasProperty("year", is("2015"))))
                    .andExpect(view().name("itBooks/details"));
    }

    @WithMockUser(username="reader1",authorities= {"reader"})
    @Test
    void testGetDetailsFalseIsbn13() throws Exception {
        this.mockMvc.perform(get("/itBooks/details/9781484206481"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/oups"));
    }

}