
package org.springframework.samples.petclinic.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.samples.petclinic.model.MeetingAssistant;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.samples.petclinic.service.MeetingAssistantService;
import org.springframework.samples.petclinic.service.MeetingService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.CantInscribeMeetingException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.doCallRealMethod;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = MeetingController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class MeetingControllerTests {

	private static final int TEST_BOOK_ID = 1;

	private static final int TEST_MEETING_ID = 2;

	private static final int TEST_MEETINGASSISTANT_ID = 1;

	private static final int TEST_MEETINGASSISTANT_ID2 = 2;

	private static final int TEST_BOOK_ID2 = 2;

	private static final int TEST_MEETING_ASSISTANT_ID = 2;

	private static final int TEST_MEETING_ID2 = 3;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MeetingService meetingService;

	@MockBean
	private MeetingAssistantService meetingAssistantService;

	@MockBean
	private BookService bookService;

	@MockBean
	private LocalDateTimeFormatter localDateTimeFormatter;

	@MockBean
	private UserService userService;

	private Meeting meeting;

	@BeforeEach
	void setup() throws ParseException, CantInscribeMeetingException {
		Book book = new Book();
		book.setId(TEST_BOOK_ID);

		LocalDateTime begin = LocalDateTime.of(2020, 10, 10, 17, 00, 00);
		LocalDateTime end = LocalDateTime.of(2020, 10, 10, 19, 00, 00);
		this.meeting = new Meeting();
		this.meeting.setName("Meeting");
		this.meeting.setPlace("Library");
		this.meeting.setStart(begin);
		this.meeting.setEnd(end);
		this.meeting.setCapacity(50);
		this.meeting.setBook(book);
		this.meeting.setId(TEST_MEETING_ID);

		Collection<Meeting> meetingsOneResult = new ArrayList<>();
		meetingsOneResult.add(this.meeting);

		Collection<Meeting> meetingsSeveralResults = new ArrayList<>();
		meetingsSeveralResults.add(new Meeting());
		meetingsSeveralResults.add(new Meeting());

		when(meetingService.findMeetingById(TEST_MEETING_ID)).thenReturn(this.meeting);
		when(meetingService.findMeetingsByNamePlaceBookTile("One result")).thenReturn(meetingsOneResult);
		when(meetingService.findMeetingsByNamePlaceBookTile("Several results")).thenReturn(meetingsSeveralResults);

		when(localDateTimeFormatter.parse(eq("2020-10-10T17:00"), any(Locale.class))).thenReturn(begin);
		when(localDateTimeFormatter.parse(eq("2020-10-10T19:00"), any(Locale.class))).thenReturn(end);

		User user = new User();
		user.setEnabled(true);
		user.setUsername("spring");
		user.setPassword("spring");
		when(userService.findUserByUsername(user.getUsername())).thenReturn(user);
	}

	@WithMockUser(value = "spring")
	@Test
	void testGetMeetingsNoFound() throws Exception {
		this.mockMvc.perform(get("/meetings").param("name", "Unknown Name")).andExpect(status().isOk())
				.andExpect(model().attributeHasFieldErrors("meeting", "name"))
				.andExpect(model().attributeHasFieldErrorCode("meeting", "name", "notFound"))
				.andExpect(view().name("meetings/findMeetings"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testGetMeeting() throws Exception {
		LocalDateTime begin = LocalDateTime.of(2020, 10, 10, 17, 00, 00);
		LocalDateTime end = LocalDateTime.of(2020, 10, 10, 19, 00, 00);
		this.mockMvc.perform(get("/meetings/{meetingId}", TEST_MEETING_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("assistants")).andExpect(model().attributeExists("remainingSeats"))
				.andExpect(MockMvcResultMatchers.model().attribute("meeting",
						Matchers.hasProperty("name", Matchers.is("Meeting"))))
				.andExpect(MockMvcResultMatchers.model().attribute("meeting",
						Matchers.hasProperty("place", Matchers.is("Library"))))
				.andExpect(MockMvcResultMatchers.model().attribute("meeting",
						Matchers.hasProperty("capacity", Matchers.is(50))))
				.andExpect(MockMvcResultMatchers.model().attribute("meeting",
						Matchers.hasProperty("start", Matchers.is(begin))))
				.andExpect(MockMvcResultMatchers.model().attribute("meeting",
						Matchers.hasProperty("end", Matchers.is(end))))
				.andExpect(view().name("meetings/meetingDetails"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitFindForm() throws Exception {
		this.mockMvc.perform(get("/meetings/find")).andExpect(status().isOk())
				.andExpect(model().attributeExists("meeting")).andExpect(view().name("meetings/findMeetings"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFindMeetingsNoFound() throws Exception {
		this.mockMvc.perform(get("/meetings").param("name", "Unknown Name")).andExpect(status().isOk())
				.andExpect(model().attributeHasFieldErrors("meeting", "name"))
				.andExpect(model().attributeHasFieldErrorCode("meeting", "name", "notFound"))
				.andExpect(view().name("meetings/findMeetings"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testGetMeetingsOneFound() throws Exception {
		this.mockMvc.perform(get("/meetings").param("name", "One result")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/meetings/" + TEST_MEETING_ID));
	}

	@WithMockUser(value = "spring")
	@Test
	void testGetMeetingsSeveralFound() throws Exception {
		this.mockMvc.perform(get("/meetings").param("name", "Several results")).andExpect(status().isOk())
				.andExpect(model().attributeExists("meetings")).andExpect(view().name("meetings/meetingsList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testAddMeeting() throws Exception {
		Book book = new Book();
		book.setId(MeetingControllerTests.TEST_BOOK_ID);
		book.setVerified(true);
		Mockito.when(this.bookService.findBookById(MeetingControllerTests.TEST_BOOK_ID)).thenReturn(book);
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/admin/books/{bookId}/meetings/new",
						MeetingControllerTests.TEST_BOOK_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("meeting"))
				.andExpect(MockMvcResultMatchers.view().name("meetings/meetingAdd"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testAddMeetingNotVerifiedBook() throws Exception {
		Book book = new Book();
		book.setId(MeetingControllerTests.TEST_BOOK_ID);
		book.setVerified(false);
		Mockito.when(this.bookService.findBookById(MeetingControllerTests.TEST_BOOK_ID)).thenReturn(book);
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/admin/books/{bookId}/meetings/new",
						MeetingControllerTests.TEST_BOOK_ID))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/oups"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSaveMeeting() throws Exception {
		Book book = new Book();
		book.setId(MeetingControllerTests.TEST_BOOK_ID);
		book.setVerified(true);
		Mockito.when(this.bookService.findBookById(MeetingControllerTests.TEST_BOOK_ID)).thenReturn(book);
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/admin/books/{bookId}/meetings/new", MeetingControllerTests.TEST_BOOK_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Test name")
						.param("place", "Test place").param("capacity", "50").param("start", "2020-10-10T17:00")
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
				.perform(MockMvcRequestBuilders
						.post("/admin/books/{bookId}/meetings/new", MeetingControllerTests.TEST_BOOK_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "").param("place", "")
						.param("capacity", "1").param("start", "2020-10-1017:00").param("end", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("meeting"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("meeting", "name"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("meeting", "place"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("meeting", "capacity"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("meeting", "start"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("meeting", "end"))
				.andExpect(MockMvcResultMatchers.view().name("meetings/meetingAdd"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testUnsubscribe() throws Exception {
		Mockito.when(this.meetingAssistantService
				.findMeetingAssistantByUsernameAndMeetingId(MeetingControllerTests.TEST_MEETING_ID, "spring"))
				.thenReturn(Optional.of(1));
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/meetings/{meetingId}/unsuscribe",
						MeetingControllerTests.TEST_MEETING_ASSISTANT_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("mensaje", "You are successfully unsubscribed"))
				.andExpect(MockMvcResultMatchers.view().name("meetings/meetingDetails"));

	}

	@WithMockUser(value = "spring")
	@Test
	void testUnsubscribeError() throws Exception {
		Mockito.when(this.meetingAssistantService
				.findMeetingAssistantByUsernameAndMeetingId(MeetingControllerTests.TEST_MEETING_ID, "spring"))
				.thenReturn(Optional.empty());
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/meetings/{meetingId}/unsuscribe",
						MeetingControllerTests.TEST_MEETING_ASSISTANT_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("mensaje", "You are not suscribed!"))
				.andExpect(MockMvcResultMatchers.view().name("meetings/meetingDetails"));

	}

	@WithMockUser(value = "spring")
	@Test
	void testUnsubscribeErrorDate() throws Exception {
		this.meeting.setStart(LocalDateTime.of(2019, 10, 10, 17, 00, 00));
		this.meeting.setEnd(LocalDateTime.of(2019, 10, 10, 19, 00, 00));
		Mockito.when(this.meetingAssistantService
				.findMeetingAssistantByUsernameAndMeetingId(MeetingControllerTests.TEST_MEETING_ID, "spring"))
				.thenReturn(Optional.of(1));
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/meetings/{meetingId}/unsuscribe",
						MeetingControllerTests.TEST_MEETING_ASSISTANT_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("mensaje", "The meeting has already been held!"))
				.andExpect(MockMvcResultMatchers.view().name("meetings/meetingDetails"));

	}

	@WithMockUser(value = "spring")
	@Test
	void testSaveMeetingNotVerifiedBook() throws Exception {
		Book book = new Book();
		book.setId(TEST_BOOK_ID);
		book.setVerified(false);
		when(bookService.findBookById(TEST_BOOK_ID)).thenReturn(book);
		doCallRealMethod().when(meetingService).addMeeting(any(Meeting.class));
		mockMvc.perform(post("/admin/books/{bookId}/meetings/new", TEST_BOOK_ID).with(csrf()).param("name", "Test name")
				.param("place", "Test place").param("capacity", "50").param("start", "2020-10-10T17:00")
				.param("end", "2020-10-10T19:00")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/oups"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInscribeReunion() throws Exception {
		when(meetingAssistantService.canInscribe(TEST_MEETING_ID2, "spring", TEST_BOOK_ID)).thenReturn(true);
		mockMvc.perform(get("/meetings/{meetingId}/inscribe", TEST_MEETING_ID2)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/meetings"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInscribeReunionWithErrors() throws Exception {
		doCallRealMethod().when(meetingAssistantService).save(any(MeetingAssistant.class));
		when(meetingAssistantService.canInscribe(TEST_MEETING_ID, "spring", TEST_BOOK_ID)).thenReturn(false);
		mockMvc.perform(get("/meetings/{meetingId}/inscribe", TEST_MEETING_ID)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/oups"));
	}

}
