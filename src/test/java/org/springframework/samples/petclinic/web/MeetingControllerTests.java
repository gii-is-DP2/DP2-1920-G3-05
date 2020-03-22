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
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.samples.petclinic.service.MeetingService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers=MeetingController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class MeetingControllerTests {

    private static final int TEST_BOOK_ID = 1;

    private static final int TEST_MEETING_ID = 2;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeetingService meetingService;

    @MockBean
    private BookService bookService;

    @MockBean
    private LocalDateTimeFormatter localDateTimeFormatter;

    @BeforeEach
    void setup() throws ParseException{
        Book book = new Book();
        book.setId(TEST_BOOK_ID);

        Meeting meeting = new Meeting();
        LocalDateTime begin = LocalDateTime.of(2020, 10, 10, 17, 00, 00);
        LocalDateTime end = LocalDateTime.of(2020, 10, 10, 19, 00, 00);
        meeting.setName("Meeting");
        meeting.setPlace("Library");
        meeting.setStart(begin);
        meeting.setEnd(end);
        meeting.setCapacity(50);
        meeting.setBook(book);
        meeting.setId(TEST_MEETING_ID);

        when(meetingService.findMeetingById(TEST_MEETING_ID)).thenReturn(meeting);

        when(localDateTimeFormatter.parse(eq("2020-10-10T17:00"), any(Locale.class))).thenReturn(begin);
        when(localDateTimeFormatter.parse(eq("2020-10-10T19:00"), any(Locale.class))).thenReturn(end);

    }

    @WithMockUser(value = "spring")
    @Test
    void testGetMeetingsNoFound() throws Exception {
        when(meetingService.findAllMeetings()).thenReturn(new ArrayList<Meeting>());
        mockMvc.perform(get("/meetings"))
                .andExpect(status().isOk())
                .andExpect(view().name("meetings/meetingsList"));
    }

    @WithMockUser(value = "spring")
    @Test
    void testGetMeetingsOneFound() throws Exception {
        List<Meeting> meetings = new ArrayList<Meeting>();
        Meeting meeting = new Meeting();
        meeting.setId(TEST_MEETING_ID);
        meetings.add(meeting);
        when(meetingService.findAllMeetings()).thenReturn(meetings);
        mockMvc.perform(get("/meetings"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meetings/" + TEST_MEETING_ID));
    }

    @WithMockUser(value = "spring")
    @Test
    void testGetMeetingsSeveralFound() throws Exception {
        List<Meeting> meetings = new ArrayList<Meeting>();
        meetings.add(new Meeting());
        meetings.add(new Meeting());
        when(meetingService.findAllMeetings()).thenReturn(meetings);
        mockMvc.perform(get("/meetings"))
                .andExpect(status().isOk())
                .andExpect(view().name("meetings/meetingsList"));
    }

    @WithMockUser(value = "spring")
    @Test
    void testShowMeeting() throws Exception {
        mockMvc.perform(get("/meetings/{meetingId}", TEST_MEETING_ID))
            .andExpect(status().isOk())
            .andExpect(model().attribute("meeting", hasProperty("name", is("Meeting"))))
            .andExpect(model().attribute("meeting", hasProperty("capacity", is(50))))
            .andExpect(model().attribute("meeting", hasProperty("place", is("Library"))))
            .andExpect(model().attribute("meeting", hasProperty("start", is(LocalDateTime.of(2020, 10, 10, 17, 00, 00)))))
            .andExpect(model().attribute("meeting", hasProperty("end", is(LocalDateTime.of(2020, 10, 10, 19, 00, 00)))))
            .andExpect(view().name("meetings/meetingDetails"));
    }

    @WithMockUser(value = "spring")
    @Test
    void testAddMeeting() throws Exception {
        Book book = new Book();
        book.setId(TEST_BOOK_ID);
        book.setVerified(true);
        when(bookService.findBookById(TEST_BOOK_ID)).thenReturn(book);
        mockMvc.perform(get("/admin/books/{bookId}/meetings/new", TEST_BOOK_ID))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("meeting"))
               .andExpect(view().name("meetings/meetingAdd"));
    }

    @WithMockUser(value = "spring")
    @Test
    void testAddMeetingNotVerifiedBook() throws Exception {
        Book book = new Book();
        book.setId(TEST_BOOK_ID);
        book.setVerified(false);
        when(bookService.findBookById(TEST_BOOK_ID)).thenReturn(book);
        mockMvc.perform(get("/admin/books/{bookId}/meetings/new", TEST_BOOK_ID))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/oups"));
    }

    @WithMockUser(value = "spring")
    @Test
    void testSaveMeeting() throws Exception {
        Book book = new Book();
        book.setId(TEST_BOOK_ID);
        book.setVerified(true);
        when(bookService.findBookById(TEST_BOOK_ID)).thenReturn(book);
        mockMvc.perform(post("/admin/books/{bookId}/meetings/new", TEST_BOOK_ID)
                .with(csrf())
                .param("name", "Test name")
                .param("place", "Test place")
                .param("capacity", "50")
                .param("start", "2020-10-10T17:00")
                .param("end", "2020-10-10T19:00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meetings"));
    }

    @WithMockUser(value = "spring")
    @Test
    void testSaveWithErrors() throws Exception {
        Book book = new Book();
        book.setId(TEST_BOOK_ID);
        book.setVerified(true);
        when(bookService.findBookById(TEST_BOOK_ID)).thenReturn(book);
        mockMvc.perform(post("/admin/books/{bookId}/meetings/new", TEST_BOOK_ID)
                .with(csrf())
                .param("name", "")
                .param("place", "")
                .param("capacity", "1")
                .param("start", "2020-10-1017:00")
                .param("end", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("meeting"))
                .andExpect(model().attributeHasFieldErrors("meeting", "name"))
                .andExpect(model().attributeHasFieldErrors("meeting", "place"))
                .andExpect(model().attributeHasFieldErrors("meeting", "capacity"))
                .andExpect(model().attributeHasFieldErrors("meeting", "start"))
                .andExpect(model().attributeHasFieldErrors("meeting", "end"))
                .andExpect(view().name("meetings/meetingAdd"));
    }

    @WithMockUser(value = "spring")
    @Test
    void testSaveMeetingNotVerifiedBook() throws Exception {
        Book book = new Book();
        book.setId(TEST_BOOK_ID);
        book.setVerified(false);
        when(bookService.findBookById(TEST_BOOK_ID)).thenReturn(book);
        doCallRealMethod().when(meetingService).addMeeting(any(Meeting.class));
        mockMvc.perform(post("/admin/books/{bookId}/meetings/new", TEST_BOOK_ID)
                .with(csrf())
                .param("name", "Test name")
                .param("place", "Test place")
                .param("capacity", "50")
                .param("start", "2020-10-10T17:00")
                .param("end", "2020-10-10T19:00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/oups"));
    }
}
