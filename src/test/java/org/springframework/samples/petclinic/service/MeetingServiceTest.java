package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.samples.petclinic.service.exceptions.NotVerifiedBookMeetingException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class MeetingServiceTest {

	@Autowired
	private MeetingService sut;
	
	@Autowired
	private MeetingAssistantService assistantsService;
	
	@Autowired
	private BookService bookService;

	@Test
	void shouldGetMeetingsIdsFromBookId() {
		int bookId = 1;
		List<Integer> meetingsId = this.sut.getMeetingsIdFromBook(bookId);
		Assertions.assertThat(meetingsId.size()).isEqualTo(1);
		Assertions.assertThat(meetingsId).contains(2);
		
		bookId = 7;
		meetingsId = this.sut.getMeetingsIdFromBook(bookId);
		Assertions.assertThat(meetingsId.size()).isEqualTo(1);
		Assertions.assertThat(meetingsId).contains(1);

		bookId = 3;
		meetingsId = this.sut.getMeetingsIdFromBook(bookId);
		Assertions.assertThat(meetingsId).isEmpty();
	}
	
	@Test
	void shouldDeleteMeetingWithAssistants() {
		int meetingId = 1;
		
		List<Integer> assistantsIds = this.assistantsService.getAssistantsMeeting(meetingId);
		
		this.sut.deleteMeeting(meetingId);
		
		Boolean existsMeeting = this.sut.existsMeetingById(meetingId);
		Assertions.assertThat(existsMeeting).isFalse();
		for(Integer i: assistantsIds) {
			Boolean existsAssistantToMeeting = this.assistantsService.existsAssistantById(i);
			Assertions.assertThat(existsAssistantToMeeting).isFalse();
		}
	}
	
	@Test
	void shouldDeleteMeetingWithoutAssistants() {
		int meetingId = 4;
				
		this.sut.deleteMeeting(meetingId);
		
		Boolean existsMeeting = this.sut.existsMeetingById(meetingId);
		Assertions.assertThat(existsMeeting).isFalse();		
	}

	@Test
	void shouldAddMeeting() {
		Meeting meeting = new Meeting();
		Book book = this.bookService.findBookById(1);
		LocalDateTime start = LocalDateTime.of(2100,10,10,16,00,00);
		LocalDateTime end = LocalDateTime.of(2100,10,10,18,00,00);
		meeting.setBook(book);
		meeting.setName("name");
		meeting.setPlace("place");
		meeting.setStart(start);
		meeting.setEnd(end);
		meeting.setCapacity(20);
		try{
		this.sut.addMeeting(meeting);
		}catch(NotVerifiedBookMeetingException e){}
		//Le toca id 5
		Meeting saved = this.sut.findMeetingById(5);
		Assertions.assertThat(saved.getName()).isEqualTo(meeting.getName());
		Assertions.assertThat(saved.getPlace()).isEqualTo(meeting.getPlace());
		Assertions.assertThat(saved.getStart()).isEqualTo(meeting.getStart());
		Assertions.assertThat(saved.getEnd()).isEqualTo(meeting.getEnd());
		Assertions.assertThat(saved.getCapacity()).isEqualTo(meeting.getCapacity());
		Assertions.assertThat(saved.getBook().getId()).isEqualTo(meeting.getBook().getId());
	}

	@Test
	void shouldNotAddMeetingBookNotVerified() throws NotVerifiedBookMeetingException{
		Meeting meeting = new Meeting();
		Book book = this.bookService.findBookById(3);
		LocalDateTime start = LocalDateTime.of(2100,10,10,16,00,00);
		LocalDateTime end = LocalDateTime.of(2100,10,10,18,00,00);
		meeting.setBook(book);
		meeting.setName("name");
		meeting.setPlace("place");
		meeting.setStart(start);
		meeting.setEnd(end);
		meeting.setCapacity(20);
		assertThrows(NotVerifiedBookMeetingException.class, () -> this.sut.addMeeting(meeting));
	}

}
