package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.ItApiSearch;
import org.springframework.samples.petclinic.model.ItBook;
import org.springframework.samples.petclinic.model.ItBookDetails;
import org.springframework.samples.petclinic.service.ItApiService;
import org.springframework.samples.petclinic.service.exceptions.BadIsbnException;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = ItApiController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
 class ItApiControllerTests {

    @Autowired
    private MockMvc         mockMvc;
    
    @MockBean
    private ItApiService    itApiService;

    @BeforeEach
    void setup() throws BadIsbnException {
        ItApiSearch search = new ItApiSearch();
        List<ItBook> itBooks = new ArrayList<>();
        itBooks.add(new ItBook());
        itBooks.add(new ItBook());
        search.setBooks(itBooks);
        when(itApiService.searchItBooks(anyString())).thenReturn(search);
        ItBookDetails itBookDetails = new ItBookDetails();
        itBookDetails.setTitle("test title");
        itBookDetails.setSubtitle("test subtitle");
        itBookDetails.setImage("https://www.example.com");
        itBookDetails.setIsbn13("1234567890123");
        itBookDetails.setIsbn10("1234567890");
        itBookDetails.setAuthors("test authors");
        itBookDetails.setDesc("test description");
        itBookDetails.setLanguage("English");
        itBookDetails.setPages("100");
        itBookDetails.setPrice("10");
        itBookDetails.setPublisher("test publisher");
        itBookDetails.setRating("5");
        itBookDetails.setUrl("https://www.url.com");
        itBookDetails.setYear("2020");
        when(itApiService.getDetailsItBook(anyString())).thenReturn(itBookDetails);
    }

    @WithMockUser(value = "spring")
    @Test
    void testSearchForm() throws Exception {
        this.mockMvc.perform(get("/itBooks/find"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("itBook"))
                    .andExpect(view().name("itBooks/find"));
    }
    
    @WithMockUser(value = "spring")
    @Test
    void testProcessFindForm() throws Exception {
        this.mockMvc.perform(get("/itBooks").param("title", "apache"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("itBooks"))
                    .andExpect(view().name("itBooks/list"));
    }

    @WithMockUser(value = "spring")
    @Test
    void testGetDetails() throws Exception {
        this.mockMvc.perform(get("/itBooks/details/9781484206485"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("itBook"))
                    .andExpect(model().attribute("itBook", hasProperty("title", is("test title"))))
                    .andExpect(model().attribute("itBook", hasProperty("subtitle", is("test subtitle"))))
                    .andExpect(model().attribute("itBook", hasProperty("image", is("https://www.example.com"))))
                    .andExpect(model().attribute("itBook", hasProperty("isbn13", is("1234567890123"))))
                    .andExpect(model().attribute("itBook", hasProperty("isbn10", is("1234567890"))))
                    .andExpect(model().attribute("itBook", hasProperty("authors", is("test authors"))))
                    .andExpect(model().attribute("itBook", hasProperty("desc", is("test description"))))
                    .andExpect(model().attribute("itBook", hasProperty("language", is("English"))))
                    .andExpect(model().attribute("itBook", hasProperty("pages", is("100"))))
                    .andExpect(model().attribute("itBook", hasProperty("price", is("10"))))
                    .andExpect(model().attribute("itBook", hasProperty("publisher", is("test publisher"))))
                    .andExpect(model().attribute("itBook", hasProperty("rating", is("5"))))
                    .andExpect(model().attribute("itBook", hasProperty("url", is("https://www.url.com"))))
                    .andExpect(model().attribute("itBook", hasProperty("year", is("2020"))))
                    .andExpect(view().name("itBooks/details"));
    }

    @WithMockUser(value = "spring")
    @Test
    void testGetDetailsFalseIsbn13() throws Exception {
        doCallRealMethod().when(itApiService).getDetailsItBook("9781484206481");
        this.mockMvc.perform(get("/itBooks/details/9781484206481"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/oups"));
    }

}