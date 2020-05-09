package org.springframework.samples.petclinic.web.e2e;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;

import org.hamcrest.Matchers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
  webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class MeetingControllerE2ETest {
	
	private static final int TEST_MEETING_ID = 1;


	@Autowired
	private MockMvc mockMvc;	

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testGetMeetingsNoFound() throws Exception {
		this.mockMvc.perform(get("/meetings").param("name", "Unknown Name")).andExpect(status().isOk())
				.andExpect(model().attributeHasFieldErrors("meeting", "name"))
				.andExpect(model().attributeHasFieldErrorCode("meeting", "name", "notFound"))
				.andExpect(view().name("meetings/findMeetings"));
	}
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testInitFindForm() throws Exception {
		this.mockMvc.perform(get("/meetings/find")).andExpect(status().isOk())
				.andExpect(model().attributeExists("meeting")).andExpect(model().attributeExists("quote")).andExpect(view().name("meetings/findMeetings"));
	}
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testGetMeeting() throws Exception {
		
		LocalDateTime begin = LocalDateTime.of(2020, 10, 20, 19, 30, 00);
		LocalDateTime end = LocalDateTime.of(2020, 10, 20, 21, 00, 00);
		this.mockMvc.perform(get("/meetings/{meetingId}", TEST_MEETING_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("assistants")).andExpect(model().attributeExists("remainingSeats"))
				.andExpect(MockMvcResultMatchers.model().attribute("meeting",
						Matchers.hasProperty("name", Matchers.is("Primera reunion"))))
				.andExpect(MockMvcResultMatchers.model().attribute("meeting",
						Matchers.hasProperty("place", Matchers.is("Circulo joven de Los Palacios"))))
				.andExpect(MockMvcResultMatchers.model().attribute("meeting",
						Matchers.hasProperty("capacity", Matchers.is(30))))
				.andExpect(MockMvcResultMatchers.model().attribute("meeting",
						Matchers.hasProperty("start", Matchers.is(begin))))
				.andExpect(MockMvcResultMatchers.model().attribute("meeting",
						Matchers.hasProperty("end", Matchers.is(end))))
				.andExpect(view().name("meetings/meetingDetails"));
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testGetMeetingsOneFound() throws Exception {
		this.mockMvc.perform(get("/meetings").param("name", "Primera reunion")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/meetings/" + TEST_MEETING_ID));
	}
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testGetMeetingsSeveralFound() throws Exception {
		this.mockMvc.perform(get("/meetings").param("name", "")).andExpect(status().isOk())
				.andExpect(model().attributeExists("meetings")).andExpect(view().name("meetings/meetingsList"));
	}
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testAddMeeting() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/admin/books/{bookId}/meetings/new",
						1))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("meeting"))
				.andExpect(MockMvcResultMatchers.view().name("meetings/meetingAdd"));
	}
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testAddMeetingNotVerifiedBook() throws Exception {
	this.mockMvc
				.perform(MockMvcRequestBuilders.get("/admin/books/{bookId}/meetings/new",
						6))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/oups"));
	}
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testSaveMeeting() throws Exception {
	this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/admin/books/{bookId}/meetings/new", 1)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Test name")
						.param("place", "Test place").param("capacity", "50").param("start", "2020-10-10T17:00")
						.param("end", "2020-10-10T19:00"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/meetings"));
	}
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testSaveWithErrors() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/admin/books/{bookId}/meetings/new", 1)
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
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testUnsubscribe() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/meetings/{meetingId}/unsuscribe",
						1))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("mensaje", "You are successfully unsubscribed"))
				.andExpect(MockMvcResultMatchers.view().name("meetings/meetingDetails"));
	}
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testUnsubscribeError() throws Exception {
				this.mockMvc
				.perform(MockMvcRequestBuilders.get("/meetings/{meetingId}/unsuscribe",
						4))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("mensaje", "You are not suscribed!"))
				.andExpect(MockMvcResultMatchers.view().name("meetings/meetingDetails"));
	}
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testUnsubscribeErrorDate() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/meetings/{meetingId}/unsuscribe",
						5))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("mensaje", "The meeting has already been held!"))
				.andExpect(MockMvcResultMatchers.view().name("meetings/meetingDetails"));

	}
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testSaveMeetingNotVerifiedBook() throws Exception {
		mockMvc.perform(post("/admin/books/{bookId}/meetings/new", 6).with(csrf()).param("name", "Test name")
				.param("place", "Test place").param("capacity", "50").param("start", "2020-10-10T17:00")
				.param("end", "2020-10-10T19:00")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/oups"));
	}
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testInscribeReunion() throws Exception {
		mockMvc.perform(get("/meetings/{meetingId}/inscribe", 4)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/meetings"));
	}
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testInscribeReunionWithErrors() throws Exception {
		mockMvc.perform(get("/meetings/{meetingId}/inscribe", 5))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/oups"));
	}


}
