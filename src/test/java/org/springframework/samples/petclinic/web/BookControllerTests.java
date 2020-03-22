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
import org.springframework.samples.petclinic.model.Genre;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.samples.petclinic.model.Review;
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
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;


@WebMvcTest(controllers=BookController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
class BookControllerTests {

    private static final int TEST_BOOK_ID = 1;

    @MockBean
    private UserService userservice;

    @Autowired
    private BookController bookController; 

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private MeetingService meetingService;

    @MockBean
    private MeetingAssistantService meetingAssistantService;

    @MockBean
    private NewService newService;

    @MockBean 
    private BookInNewService bookNewService;

    @MockBean
    private PublicationService publicationService;

    @MockBean
    private ReadBookService readBookService;

    @MockBean 
    private WishedBookService wishedBookService;


	@BeforeEach
	void setup() {
		/*book = new Book();
		book.setId(TEST_BOOK_ID);
        book.setTitle("title");	
        book.setAuthor("author");
        book.setEditorial("editorial");
        Genre genre = new Genre();
        genre.setName("name");
        book.setGenre(new Genre());
        book.setISBN("9780345805362");
        book.setImage("https://pictures.abebooks.com/isbn/9780575081406-es.jpg");
		book.setPages(100);
        book.setPublicationDate(LocalDate.now());
        book.setSynopsis("He robado princesas a reyes agónicos. Incendié la ciudad de Trebon. He pasado la noche con Felurian y he despertado vivo y cuerdo. Me expulsaron de la Universidad a una edad a la que a la mayoría todavía no los dejan entrar. He recorrido de noche caminos de los que otros no se atreven a hablar ni siquiera de día. He hablado con dioses, he amado a mujeres y he escrito canciones que hacen llorar a los bardos. Me llamo Kvothe. Quizá hayas oído hablar de mí");
*/
        when(reviewService.getReviewsIdFromBook(TEST_BOOK_ID)).thenReturn(new ArrayList<Integer>());
        when(meetingService.getMeetingsIdFromBook(TEST_BOOK_ID)).thenReturn(new ArrayList<Integer>());
        when(newService.getNewsFromBook(TEST_BOOK_ID)).thenReturn(new ArrayList<Integer>());
        when(publicationService.getPublicationsFromBook(TEST_BOOK_ID)).thenReturn(new ArrayList<Integer>());
        when(publicationService.getPublicationsFromBook(TEST_BOOK_ID)).thenReturn(new ArrayList<Integer>());
        doNothing().when(readBookService).deleteReadBookByBookId(TEST_BOOK_ID);
        doNothing().when(wishedBookService).deleteByBookId(TEST_BOOK_ID);
        doNothing().when(bookService).deleteById(TEST_BOOK_ID, "spring");
	}

    @WithMockUser(value = "spring")
    @Test
    void testDelete() throws Exception{
        mockMvc.perform(get("/admin/books/delete/{bookId}", TEST_BOOK_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
                
    }
}
