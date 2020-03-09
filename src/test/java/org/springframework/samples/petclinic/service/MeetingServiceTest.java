package org.springframework.samples.petclinic.service;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class MeetingServiceTest {

	@Autowired
	private MeetingService sut;
	
	@Autowired
	private MeetingAssistantService assistantsService;
	
	@Test
	void shouldGetMeetingsIdsFromBookId() {
		int bookId = 1;
		List<Integer> meetingsId = this.sut.getMeetingsFromBook(bookId);
		Assertions.assertThat(meetingsId.size()).isEqualTo(1);
		Assertions.assertThat(meetingsId).contains(2);
		
		bookId = 7;
		meetingsId = this.sut.getMeetingsFromBook(bookId);
		Assertions.assertThat(meetingsId.size()).isEqualTo(1);
		Assertions.assertThat(meetingsId).contains(1);

		bookId = 3;
		meetingsId = this.sut.getMeetingsFromBook(bookId);
		Assertions.assertThat(meetingsId).isEmpty();
	}
	
	@Test
	void shouldDeleteMeetingWithAssistants() {
		int meetingId = 1;
		Boolean existsMeeting = this.sut.existsMeetingById(meetingId);
		Assertions.assertThat(existsMeeting).isTrue();
		
		List<Integer> assistantsIds = this.assistantsService.getAssistantsMeeting(meetingId);
		for(Integer i: assistantsIds) {
			Boolean existsAssistantToMeeting = this.assistantsService.existsAssistantById(i);
			Assertions.assertThat(existsAssistantToMeeting).isTrue();
		}
		
		this.sut.deleteMeeting(meetingId);
		
		existsMeeting = this.sut.existsMeetingById(meetingId);
		Assertions.assertThat(existsMeeting).isFalse();
		for(Integer i: assistantsIds) {
			Boolean existsAssistantToMeeting = this.assistantsService.existsAssistantById(i);
			Assertions.assertThat(existsAssistantToMeeting).isFalse();
		}
	}
	
	@Test
	void shouldDeleteMeetingWithoutAssistants() {
		int meetingId = 4;
		Boolean existsMeeting = this.sut.existsMeetingById(meetingId);
		Assertions.assertThat(existsMeeting).isTrue();
		
		List<Integer> assistantsIds = this.assistantsService.getAssistantsMeeting(meetingId);
		Assertions.assertThat(assistantsIds).isEmpty();
		
		this.sut.deleteMeeting(meetingId);
		
		existsMeeting = this.sut.existsMeetingById(meetingId);
		Assertions.assertThat(existsMeeting).isFalse();		
	}
}
