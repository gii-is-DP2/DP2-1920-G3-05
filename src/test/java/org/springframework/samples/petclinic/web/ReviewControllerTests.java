package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Review;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.samples.petclinic.service.ReviewService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.CantDeleteReviewException;
import org.springframework.samples.petclinic.service.exceptions.CantEditReviewException;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;


@WebMvcTest(controllers=ReviewController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class ReviewControllerTests {

    private static final int TEST_BOOK_ID = 1;

    private static final int TEST_BOOK_ID_2 = 2;

    private static final int TEST_BOOK_ID_3 = 3;

    private static final int TEST_REVIEW_ID = 4;

    private static final int TEST_REVIEW_ID_2 = 5;


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean
	private BookService bookService;

    @MockBean
    private UserService userService;
    
    @BeforeEach
    void setup() throws CantEditReviewException, CantDeleteReviewException{
        Book book1 = new Book();
        book1.setId(TEST_BOOK_ID);
        when(bookService.findBookById(TEST_BOOK_ID)).thenReturn(book1);
        Book book2 = new Book();
        book2.setId(TEST_BOOK_ID_2);
        when(bookService.findBookById(TEST_BOOK_ID_2)).thenReturn(book2);
        when(userService.findUserByUsername("spring")).thenReturn(new User());

        when(reviewService.canWriteReview(TEST_BOOK_ID, "spring")).thenReturn(true);

        Review review = new Review();
        review.setId(TEST_REVIEW_ID);
        review.setBook(new Book());
        review.setOpinion("Test opinion");
        review.setRaiting(2);
        review.setTitle("Test title");
        review.setUser(new User());
        when(reviewService.findReviewById(TEST_REVIEW_ID)).thenReturn(review);

        Review review2 = new Review();
        review2.setId(TEST_REVIEW_ID_2);
        review2.setBook(new Book());
        review2.setOpinion("Test opinion");
        review2.setRaiting(2);
        review2.setTitle("Test title");
        review2.setUser(new User());
        when(reviewService.findReviewById(TEST_REVIEW_ID_2)).thenReturn(review2);

        List<Review> reviewsOneReview = new ArrayList<Review>();
        reviewsOneReview.add(review);
        when(reviewService.getReviewsFromBook(TEST_BOOK_ID)).thenReturn(reviewsOneReview);

        when(reviewService.getReviewsFromBook(TEST_BOOK_ID_2)).thenReturn(new ArrayList<Review>());

        List<Review> reviewsSeveralReview = new ArrayList<Review>();
        reviewsSeveralReview.add(new Review());
        reviewsSeveralReview.add(new Review());
        when(reviewService.getReviewsFromBook(TEST_BOOK_ID_3)).thenReturn(reviewsSeveralReview);

        when(reviewService.reviewIsMine(TEST_REVIEW_ID, "spring")).thenReturn(true);
        when(reviewService.reviewIsMine(TEST_REVIEW_ID_2, "spring")).thenReturn(false);

        doCallRealMethod().when(reviewService).deleteReviewById(TEST_REVIEW_ID_2, "spring");//Necesitamos que entre en el metodo para que lance excepcion pq no puede borrar la review
        when(reviewService.canDeleteReview(TEST_REVIEW_ID, "spring")).thenReturn(true);
        when(reviewService.canDeleteReview(TEST_REVIEW_ID_2, "spring")).thenReturn(false);
    }

    @WithMockUser(value = "spring")
    @Test
    void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/books/{bookId}/reviews/new", TEST_BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("review"))
                .andExpect(view().name("reviews/reviewAddForm"));
    }

    @WithMockUser(value = "spring")
    @Test
    void testProcessCreationForm() throws Exception{
        mockMvc.perform(post("/books/{bookId}/reviews/new", TEST_BOOK_ID)
                        .with(csrf())
                        .param("title", "Test title")
                        .param("raiting", "3")
                        .param("opinion", "Test opinion"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/reviews/**"));
    }

    @WithMockUser(value = "spring")
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

    @WithMockUser(value = "spring")
    @Test
    void testProcessCreationFormCantWriteException() throws Exception{
        doCallRealMethod().when(reviewService).writeReview(any(Review.class), eq("spring"));
        when(reviewService.canWriteReview(TEST_BOOK_ID_2, "spring")).thenReturn(false);
        mockMvc.perform(post("/books/{bookId}/reviews/new", TEST_BOOK_ID_2)
                        .with(csrf())
                        .param("title", "Test title")
                        .param("raiting", "3")
                        .param("opinion", "Test opinion"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/oups"));
    }

    @WithMockUser(value = "spring")
    @Test
    void testFindReviewsFromBookOneReview() throws Exception{
        mockMvc.perform(get("/books/{bookId}/reviews", TEST_BOOK_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reviews/" + TEST_REVIEW_ID));
    }

    @WithMockUser(value = "spring")
    @Test
    void testFindReviewsFromBookNoReviews() throws Exception{
        mockMvc.perform(get("/books/{bookId}/reviews", TEST_BOOK_ID_2))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/" + TEST_BOOK_ID_2));
    }

    @WithMockUser(value = "spring")
    @Test
    void testFindReviewsFromBookSeveralReviews() throws Exception{
        mockMvc.perform(get("/books/{bookId}/reviews", TEST_BOOK_ID_3))
                .andExpect(status().isOk())
                .andExpect(view().name("/reviews/reviewList"));
    }

    @WithMockUser(value = "spring")
    @Test
    void testShowReview() throws Exception{
        mockMvc.perform(get("/reviews/{reviewId}", TEST_REVIEW_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("review"))
                .andExpect(model().attribute("review", hasProperty("title", is("Test title"))))
                .andExpect(model().attribute("review", hasProperty("raiting", is(2))))
                .andExpect(model().attribute("review", hasProperty("opinion", is("Test opinion"))))
                .andExpect(model().attributeExists("isMine"))
                .andExpect(model().attributeExists("canDeleteReview"))
                .andExpect(view().name("reviews/reviewDetails"));
    }

    @WithMockUser(value = "spring")
    @Test
    void testFormEditMyReview() throws Exception{
        mockMvc.perform(get("/books/{bookId}/reviews/{reviewId}/edit", TEST_BOOK_ID, TEST_REVIEW_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("review"))
                .andExpect(model().attribute("review", hasProperty("title", is("Test title"))))
                .andExpect(model().attribute("review", hasProperty("raiting", is(2))))
                .andExpect(model().attribute("review", hasProperty("opinion", is("Test opinion"))))
                .andExpect(view().name("reviews/reviewUpdateForm"));
    }

    @WithMockUser(value = "spring")
    @Test
    void testFormEditOthersReview() throws Exception{
        mockMvc.perform(get("/books/{bookId}/reviews/{reviewId}/edit", TEST_BOOK_ID, TEST_REVIEW_ID_2))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/oups"));
    }

    @WithMockUser(value = "spring")
    @Test
    void testUpdateMyReview() throws Exception{
        mockMvc.perform(post("/books/{bookId}/reviews/{reviewId}/edit", TEST_BOOK_ID, TEST_REVIEW_ID)
                .with(csrf())
                .param("title", "Edited title")
                .param("raiting", "5")
                .param("opinion", "Edited opinion"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reviews/" + TEST_REVIEW_ID));
    }  
    
    @WithMockUser(value = "spring")
    @Test
    void testUpdateMyReviewWithErrors() throws Exception{
        mockMvc.perform(post("/books/{bookId}/reviews/{reviewId}/edit", TEST_BOOK_ID, TEST_REVIEW_ID)
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

    @WithMockUser(value = "spring")
    @Test
    void testUpdateOthersReview() throws Exception{
        doCallRealMethod().when(reviewService).editReview(any(Review.class), eq("spring")); 
        //Necesitamos que entre en el metodo para que lance excepcion pq no puede editar la review
        mockMvc.perform(post("/books/{bookId}/reviews/{reviewId}/edit", TEST_BOOK_ID, TEST_REVIEW_ID_2)
        .with(csrf())
        .param("title", "Edited title")
        .param("raiting", "5")
        .param("opinion", "Edited opinion"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/oups"));
    }

    @WithMockUser(value = "spring")
    @Test
    void testDeleteReview() throws Exception{
        mockMvc.perform(get("/books/{bookId}/reviews/{reviewId}/delete", TEST_BOOK_ID, TEST_REVIEW_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/" + TEST_BOOK_ID));
    }

    @WithMockUser(value = "spring")
    @Test
    void testDeleteOthersReview() throws Exception{
        mockMvc.perform(get("/books/{bookId}/reviews/{reviewId}/delete", TEST_BOOK_ID, TEST_REVIEW_ID_2))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/oups"));
    }

}
