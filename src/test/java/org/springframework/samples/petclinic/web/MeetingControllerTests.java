
package org.springframework.samples.petclinic.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.samples.petclinic.service.MeetingAssistantService;
import org.springframework.samples.petclinic.service.MeetingService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Collection;
import java.util.Locale;


@WebMvcTest(controllers=MeetingController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class MeetingControllerTests {

	private static final int		TEST_BOOK_ID				= 1;

	private static final int		TEST_MEETING_ID				= 2;

	private static final int		TEST_MEETING_ASSISTANT_ID	= 2;

	@Autowired
	private MockMvc					mockMvc;

	@MockBean
	private MeetingService			meetingService;

	@MockBean
	private MeetingAssistantService	meetingAssistantService;

	@MockBean
	private BookService				bookService;

	@MockBean
	private LocalDateTimeFormatter	localDateTimeFormatter;

    @BeforeEach
    void setup() throws ParseException{
        Book book = new Book();
        book.setId(TEST_BOOK_ID);

        LocalDateTime begin = LocalDateTime.of(2020, 10, 10, 17, 00, 00);
        LocalDateTime end = LocalDateTime.of(2020, 10, 10, 19, 00, 00);
        Meeting meeting = new Meeting();
        meeting.setName("Meeting");
        meeting.setPlace("Library");
        meeting.setStart(begin);
        meeting.setEnd(end);
        meeting.setCapacity(50);
        meeting.setBook(book);
        meeting.setId(TEST_MEETING_ID);

        Collection<Meeting> meetingsOneResult = new ArrayList<>();
        meetingsOneResult.add(meeting);

        Collection<Meeting> meetingsSeveralResults = new ArrayList<>();
        meetingsSeveralResults.add(new Meeting());
        meetingsSeveralResults.add(new Meeting());

        when(meetingService.findMeetingById(TEST_MEETING_ID)).thenReturn(meeting);
        when(meetingService.findMeetingsByNamePlaceBookTile("One result")).thenReturn(meetingsOneResult);
        when(meetingService.findMeetingsByNamePlaceBookTile("Several results")).thenReturn(meetingsSeveralResults);

        when(localDateTimeFormatter.parse(eq("2020-10-10T17:00"), any(Locale.class))).thenReturn(begin);
        when(localDateTimeFormatter.parse(eq("2020-10-10T19:00"), any(Locale.class))).thenReturn(end);
	}

	@WithMockUser(value = "spring")
	@Test
	void testGetMeetingsNoFound() throws Exception {
		this.mockMvc.perform(get("/meetings")
			.param("name", "Unknown Name"))
			.andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors("meeting", "name"))
			.andExpect(model().attributeHasFieldErrorCode("meeting", "name", "notFound"))
			.andExpect(view().name("meetings/findMeetings"));
	}

    @WithMockUser(value = "spring")
	@Test
	void testInitFindForm() throws Exception {
		this.mockMvc.perform(get("/meetings/find")).andExpect(status().isOk()).andExpect(model().attributeExists("meeting")).andExpect(view().name("meetings/findMeetings"));
	}

    @WithMockUser(value = "spring")
    @Test
    void testFindMeetingsNoFound() throws Exception {
        this.mockMvc.perform(get("/meetings").param("name", "Unknown Name")).andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors("meeting", "name"))
        .andExpect(model().attributeHasFieldErrorCode("meeting", "name", "notFound")).andExpect(view().name("meetings/findMeetings"));
    }

    @WithMockUser(value = "spring")
    @Test
    void testGetMeetingsOneFound() throws Exception {
        this.mockMvc.perform(get("/meetings").param("name", "One result"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/meetings/" + TEST_MEETING_ID));
    }

    @WithMockUser(value = "spring")
    @Test
    void testGetMeetingsSeveralFound() throws Exception {
        this.mockMvc.perform(get("/meetings").param("name", "Several results"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("meetings"))
        .andExpect(view().name("meetings/meetingsList"));
    }

	@WithMockUser(value = "spring")
	@Test
	void testAddMeeting() throws Exception {
		Book book = new Book();
		book.setId(MeetingControllerTests.TEST_BOOK_ID);
		book.setVerified(true);
		Mockito.when(this.bookService.findBookById(MeetingControllerTests.TEST_BOOK_ID)).thenReturn(book);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/books/{bookId}/meetings/new", MeetingControllerTests.TEST_BOOK_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("meeting"))
			.andExpect(MockMvcResultMatchers.view().name("meetings/meetingAdd"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testAddMeetingNotVerifiedBook() throws Exception {
		Book book = new Book();
		book.setId(MeetingControllerTests.TEST_BOOK_ID);
		book.setVerified(false);
		Mockito.when(this.bookService.findBookById(MeetingControllerTests.TEST_BOOK_ID)).thenReturn(book);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/books/{bookId}/meetings/new", MeetingControllerTests.TEST_BOOK_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.redirectedUrl("/oups"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSaveMeeting() throws Exception {
		Book book = new Book();
		book.setId(MeetingControllerTests.TEST_BOOK_ID);
		book.setVerified(true);
		Mockito.when(this.bookService.findBookById(MeetingControllerTests.TEST_BOOK_ID)).thenReturn(book);
		this.mockMvc.perform(MockMvcRequestBuilders
				.post("/admin/books/{bookId}/meetings/new", MeetingControllerTests.TEST_BOOK_ID).with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "Test name")
				.param("place", "Test place")
				.param("capacity", "50")
				.param("start", "2020-10-10T17:00")
				.param("end", "2020-10-10T19:00"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/meetings"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSaveWithErrors() throws Exception {
		Book book = new Book();
		book.setId(MeetingControllerTests.TEST_BOOK_ID);
		book.setVerified(true);
		Mockito.when(this.bookService.findBookById(MeetingControllerTests.TEST_BOOK_ID)).thenReturn(book);
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/admin/books/{bookId}/meetings/new", MeetingControllerTests.TEST_BOOK_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "").param("place", "").param("capacity", "1")
				.param("start", "2020-10-1017:00").param("end", ""))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("meeting")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("meeting", "name"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("meeting", "place")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("meeting", "capacity"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("meeting", "start")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("meeting", "end")).andExpect(MockMvcResultMatchers.view().name("meetings/meetingAdd"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSaveMeetingNotVerifiedBook() throws Exception {
		Book book = new Book();
		book.setId(MeetingControllerTests.TEST_BOOK_ID);
		book.setVerified(false);
		Mockito.when(this.bookService.findBookById(MeetingControllerTests.TEST_BOOK_ID)).thenReturn(book);
		Mockito.doCallRealMethod().when(this.meetingService).addMeeting(ArgumentMatchers.any(Meeting.class));
		this.mockMvc.perform(MockMvcRequestBuilders.post("/admin/books/{bookId}/meetings/new", MeetingControllerTests.TEST_BOOK_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Test name").param("place", "Test place")
			.param("capacity", "50").param("start", "2020-10-10T17:00").param("end", "2020-10-10T19:00")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.redirectedUrl("/oups"));
	}
	@WithMockUser(value = "spring")
	@Test
	void testUnsubscribe() throws Exception {
		Mockito.when(this.meetingAssistantService.findMeetingAssistantByUsernameAndMeetingId(MeetingControllerTests.TEST_MEETING_ID, "spring")).thenReturn(Optional.of(1));
		this.mockMvc.perform(MockMvcRequestBuilders.get("/meetings/{meetingId}/unsuscribe", MeetingControllerTests.TEST_MEETING_ASSISTANT_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("mensaje", "You are successfully unsubscribed")).andExpect(MockMvcResultMatchers.view().name("meetings/meetingDetails"));

	}
	@WithMockUser(value = "spring")
	@Test
	void testUnsubscribeError() throws Exception {
		Mockito.when(this.meetingAssistantService.findMeetingAssistantByUsernameAndMeetingId(MeetingControllerTests.TEST_MEETING_ID, "spring")).thenReturn(Optional.empty());
		this.mockMvc.perform(MockMvcRequestBuilders.get("/meetings/{meetingId}/unsuscribe", MeetingControllerTests.TEST_MEETING_ASSISTANT_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("mensaje", "You are not suscribed!")).andExpect(MockMvcResultMatchers.view().name("meetings/meetingDetails"));

	}

}
