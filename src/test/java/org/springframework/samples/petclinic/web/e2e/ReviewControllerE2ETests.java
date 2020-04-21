package org.springframework.samples.petclinic.web.e2e;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


@ExtendWith(SpringExtension.class)
@SpringBootTest(
  webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ReviewControllerE2ETests {

    private static final int TEST_BOOK_ID = 1;

    private static final int TEST_BOOK_ID_2 = 7;

    private static final int TEST_BOOK_ID_3 = 6;
    
    private static final int TEST_BOOK_ID_4 = 3;
    
    private static final int TEST_BOOK_ID_5 = 16;
    
    private static final int TEST_BOOK_ID_6 = 9;

    private static final int TEST_REVIEW_ID = 1;

    private static final int TEST_REVIEW_ID_2 = 3;
    
    private static final int TEST_REVIEW_ID_3 = 5;
    
    private static final int TEST_REVIEW_ID_4 = 12;


    @Autowired
    private MockMvc mockMvc;
    
    @WithMockUser(username = "owner1", authorities = {"reader"})
    @Test
    void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/books/{bookId}/reviews/new", TEST_BOOK_ID_3))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("review"))
                .andExpect(view().name("reviews/reviewAddForm"));
    }

    @WithMockUser(username = "reader1", authorities = {"reader"})
    @Test
    void testProcessCreationForm() throws Exception{
        mockMvc.perform(post("/books/{bookId}/reviews/new", TEST_BOOK_ID_2)
                        .with(csrf())
                        .param("title", "Test title")
                        .param("raiting", "3")
                        .param("opinion", "Test opinion"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/reviews/**"));
    }

    @WithMockUser(username = "vet1", authorities = {"reader"})
    @Test
    void testProcessCreationFormWithErrors() throws Exception{
        mockMvc.perform(post("/books/{bookId}/reviews/new", TEST_BOOK_ID)
                        .with(csrf())
                        .param("title", "")
                        .param("raiting", "6")
                        .param("opinion", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("review"))
                .andExpect(model().attributeHasFieldErrors("review", "title"))
                .andExpect(model().attributeHasFieldErrors("review", "raiting"))
                .andExpect(model().attributeHasFieldErrors("review", "opinion"))
                .andExpect(view().name("reviews/reviewAddForm"));
    }

    @WithMockUser(username = "juaferfer", authorities = {"reader"})
    @Test
    void testProcessCreationFormCantWriteException() throws Exception{
        mockMvc.perform(post("/books/{bookId}/reviews/new", TEST_BOOK_ID_2)
                        .with(csrf())
                        .param("title", "Test title")
                        .param("raiting", "3")
                        .param("opinion", "Test opinion"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/oups"));
    }

    @WithMockUser(username = "juaferfer", authorities = {"reader"})
    @Test
    void testFindReviewsFromBookOneReview() throws Exception{
        mockMvc.perform(get("/books/{bookId}/reviews", TEST_BOOK_ID_4))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reviews/" + TEST_REVIEW_ID_3));
    }

    @WithMockUser(username = "juaferfer", authorities = {"reader"})
    @Test
    void testFindReviewsFromBookNoReviews() throws Exception{
        mockMvc.perform(get("/books/{bookId}/reviews", TEST_BOOK_ID_5))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/" + TEST_BOOK_ID_5));
    }

    @WithMockUser(username = "juaferfer", authorities = {"reader"})
    @Test
    void testFindReviewsFromBookSeveralReviews() throws Exception{
        mockMvc.perform(get("/books/{bookId}/reviews", TEST_BOOK_ID_3))
                .andExpect(status().isOk())
                .andExpect(view().name("/reviews/reviewList"));
    }

    @WithMockUser(username = "juaferfer", authorities = {"reader"})
    @Test
    void testShowReview() throws Exception{
        mockMvc.perform(get("/reviews/{reviewId}", TEST_REVIEW_ID_2))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("review"))
                .andExpect(model().attribute("review", hasProperty("title", is("Libro agradable"))))
                .andExpect(model().attribute("review", hasProperty("raiting", is(3))))
                .andExpect(model().attribute("review", hasProperty("opinion", is("Es un libro fácil de leer que te hace olvidarte de tus preocupaciones"))))
                .andExpect(model().attributeExists("isMine"))
                .andExpect(model().attributeExists("canDeleteReview"))
                .andExpect(view().name("reviews/reviewDetails"));
    }

    @WithMockUser(username = "vet1", authorities = {"reader"})
    @Test
    void testFormEditMyReview() throws Exception{
        mockMvc.perform(get("/books/{bookId}/reviews/{reviewId}/edit", TEST_BOOK_ID, TEST_REVIEW_ID_3))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("review"))
                .andExpect(model().attribute("review", hasProperty("title", is("Libro para pasar el rato"))))
                .andExpect(model().attribute("review", hasProperty("raiting", is(3))))
                .andExpect(model().attribute("review", hasProperty("opinion", is("Es un libro corto y curioso"))))
                .andExpect(view().name("reviews/reviewUpdateForm"));
    }

    @WithMockUser(username = "fraperbar", authorities = {"reader"})
    @Test
    void testFormEditOthersReview() throws Exception{
        mockMvc.perform(get("/books/{bookId}/reviews/{reviewId}/edit", TEST_BOOK_ID, TEST_REVIEW_ID_2))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/oups"));
    }

    @WithMockUser(username = "reader1", authorities = {"reader"})
    @Test
    void testUpdateMyReview() throws Exception{
        mockMvc.perform(post("/books/{bookId}/reviews/{reviewId}/edit", TEST_BOOK_ID_6, TEST_REVIEW_ID_4)
                .with(csrf())
                .param("title", "Edited title")
                .param("raiting", "5")
                .param("opinion", "Edited opinion"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reviews/" + TEST_REVIEW_ID_4));
    }  
    
    @WithMockUser(username = "reader1", authorities = {"reader"})
    @Test
    void testUpdateMyReviewWithErrors() throws Exception{
        mockMvc.perform(post("/books/{bookId}/reviews/{reviewId}/edit", TEST_BOOK_ID_6, TEST_REVIEW_ID_4)
                .with(csrf())
                .param("title", "")
                .param("raiting", "6")
                .param("opinion", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("review"))
                .andExpect(model().attributeHasFieldErrors("review", "title"))
                .andExpect(model().attributeHasFieldErrors("review", "raiting"))
                .andExpect(model().attributeHasFieldErrors("review", "opinion"))
                .andExpect(view().name("reviews/reviewUpdateForm"));
    } 

    @WithMockUser(username = "fraperbar", authorities = {"reader"})
    @Test
    void testUpdateOthersReview() throws Exception{
        mockMvc.perform(post("/books/{bookId}/reviews/{reviewId}/edit", TEST_BOOK_ID, TEST_REVIEW_ID_2)
        .with(csrf())
        .param("title", "Edited title")
        .param("raiting", "5")
        .param("opinion", "Edited opinion"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/oups"));
    }

    @WithMockUser(username = "admin1", authorities = {"admin"})
    @Test
    void testDeleteReview() throws Exception{
        mockMvc.perform(get("/books/{bookId}/reviews/{reviewId}/delete", TEST_BOOK_ID, TEST_REVIEW_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/" + TEST_BOOK_ID));
    }

    @WithMockUser(username = "fraperbar", authorities = {"reader"})
    @Test
    void testDeleteOthersReview() throws Exception{
        mockMvc.perform(get("/books/{bookId}/reviews/{reviewId}/delete", TEST_BOOK_ID, TEST_REVIEW_ID_2))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/oups"));
    }

}
