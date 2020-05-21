package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

	@ParameterizedTest
	@CsvSource({
		"1,1",
		"7,2",
		"3,0"
	})
	void shouldGetMeetingsIdsFromBookId(int bookId, int results) {
		List<Integer> meetingsId = this.sut.getMeetingsIdFromBook(bookId);
		Assertions.assertThat(meetingsId.size()).isEqualTo(results);
	}
	
	@ParameterizedTest
	@CsvSource({
		"1",
		"2",
		"3"
	})
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
	
	@ParameterizedTest
	@CsvSource({
		"4"
	})
	void shouldDeleteMeetingWithoutAssistants(int meetingId) {				
		this.sut.deleteMeeting(meetingId);
		
		Boolean existsMeeting = this.sut.existsMeetingById(meetingId);
		Assertions.assertThat(existsMeeting).isFalse();		
	}

	@ParameterizedTest
	@CsvSource({
		"1",
		"2",
		"4"
	})
	void shouldAddMeeting(int bookId) {
		Meeting meeting = new Meeting();
		Book book = this.bookService.findBookById(bookId);
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
			Collection<Meeting> meetings = this.sut.findMeetingsByNamePlaceBookTile("name");
			Assertions.assertThat(meetings).hasSize(1);
		}catch(NotVerifiedBookMeetingException e){}	
	}

	@ParameterizedTest
	@CsvSource({
		"3",
		"6",
		"9"
	})
	void shouldNotAddMeetingBookNotVerified(int bookId) throws NotVerifiedBookMeetingException{
		Meeting meeting = new Meeting();
		Book book = this.bookService.findBookById(bookId);
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

	@ParameterizedTest
	@CsvSource({
		"primera,1",
		"reunion,4",
		"forum,1",
		"erorr, 0"
	})
	void sohuldFindMeetingByName(String name, int numberResults) {
		Collection<Meeting> meetings = this.sut.findMeetingsByNamePlaceBookTile(name);
		Assertions.assertThat(meetings).hasSize(numberResults);
	}

	@ParameterizedTest
	@CsvSource({
		"circulo,3",
		"nowhere,2",
		"erorr, 0"
	})
	void sohuldFindMeetingByPlace(String place, int numberResults) {
		Collection<Meeting> meetings = this.sut.findMeetingsByNamePlaceBookTile(place);
		Assertions.assertThat(meetings).hasSize(numberResults);
	}

	@ParameterizedTest
	@CsvSource({
		"it,2",
		"principito,1",
		"harry,1",
		"erorr, 0"
	})
	void sohuldFindMeetingByBookTitle(String bookTitle, int numberResults) {
		Collection<Meeting> meetings = this.sut.findMeetingsByNamePlaceBookTile(bookTitle);
		Assertions.assertThat(meetings).hasSize(numberResults);
	}

	@ParameterizedTest
	@CsvSource({
		"ETSII,2",
		"erorr, 0"
	})
	void sohuldFindMeetingByNameAndPlace(String name, int numberResults) {
		Collection<Meeting> meetings = this.sut.findMeetingsByNamePlaceBookTile(name);
		Assertions.assertThat(meetings).hasSize(numberResults);
	}

}
