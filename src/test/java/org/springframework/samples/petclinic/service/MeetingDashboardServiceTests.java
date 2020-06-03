package org.springframework.samples.petclinic.service;


import java.time.LocalDateTime;
import java.util.Calendar;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.samples.petclinic.model.MeetingDashboard;
import org.springframework.samples.petclinic.service.exceptions.NotVerifiedBookMeetingException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
 class MeetingDashboardServiceTests {

	@Autowired
	private MeetingDashboardService dashboardService;
	@Autowired
	private MeetingService meetService;
	@Autowired
	private BookService bookService;
	
	@ParameterizedTest
	@CsvSource({
		"4,15",
		"5,13"
	})
	void shouldGetDashboard(int bookId, int day) throws NotVerifiedBookMeetingException {
		Book book = this.bookService.findBookById(bookId);
		MeetingDashboard meet = this.dashboardService.getMeetingDashboard();
		Object[] meetingByDay = (Object[]) meet.getMeetingsByDay()[day-1];
		Integer meetingThisDay = 0;
		Long meetingDay =0L;
		if(!meetingByDay[1].equals(0)) {
			meetingDay = (Long) meetingByDay[1];
		}
		
		//Añadir nueva reunion ese dia del mes pasado con ese libro 
		Meeting meeting = new Meeting();
		Calendar fecha = Calendar.getInstance();
		LocalDateTime start = LocalDateTime.of(fecha.get(Calendar.YEAR),fecha.get(Calendar.MONTH),day,16,00,00);
		LocalDateTime end = LocalDateTime.of(fecha.get(Calendar.YEAR),fecha.get(Calendar.MONTH),day,18,00,00);
		meeting.setBook(book);
		meeting.setName("name");
		meeting.setPlace("place");
		meeting.setStart(start);
		meeting.setEnd(end);
		meeting.setCapacity(20);
		this.meetService.addMeeting(meeting);
		
		
		MeetingDashboard meet2 = this.dashboardService.getMeetingDashboard();
		Object[] meetingByDay2 = (Object[]) meet2.getMeetingsByDay()[day-1];
		Long meetingThisDay2 = (Long) meetingByDay2[1];
		if(meetingDay ==0L) {
			Assertions.assertThat(meetingThisDay2).isEqualTo(meetingThisDay+1);
		}else {
			Assertions.assertThat(meetingThisDay2).isEqualTo(meetingDay+1);
		}
		Assertions.assertThat(meet2.getNumberOfMeetings()).isEqualTo(meet.getNumberOfMeetings()+1);
	}
}
