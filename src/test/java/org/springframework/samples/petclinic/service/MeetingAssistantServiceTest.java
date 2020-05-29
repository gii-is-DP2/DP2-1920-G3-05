
package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.samples.petclinic.model.MeetingAssistant;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.exceptions.CantInscribeMeetingException;
import org.springframework.samples.petclinic.service.exceptions.NotVerifiedBookMeetingException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
 class MeetingAssistantServiceTest {

	@Autowired
	private MeetingAssistantService sut;
    
	@Autowired 
	private MeetingService meetingService;
	
	@Autowired
	private BookService		bookService;
	
	@Autowired
	private UserService		userService;
	
	@ParameterizedTest
	@CsvSource({
		"1,2",
		"3,2",
		"4,0"
	})
	void shouldGetMeetingAssistantsIdsFromMeetingId(int meetingId, int results) {
		List<Integer> meetingAssistantsIds = this.sut.getAssistantsMeeting(meetingId);
		Assertions.assertThat(meetingAssistantsIds.size()).isEqualTo(results);
	}
	
	@ParameterizedTest
	@CsvSource({
		"1,2",
		"3,2",
		"4,0"
	})
	void shouldGetMeetingAssistantsFromMeetingId(int meetingId, int results) {
		List<MeetingAssistant> meetingAssistants = this.sut.getAssistantsOfMeeting(meetingId);
		Assertions.assertThat(meetingAssistants.size()).isEqualTo(results);
	}
	
	@ParameterizedTest
	@CsvSource({
		"1",
		"3"
	})
	void shouldDeleteMeetingAssistantFromMeeting(int meetingId) {
		List<Integer> meetingAssistantsIds = this.sut.getAssistantsMeeting(meetingId);
		Assertions.assertThat(meetingAssistantsIds.size()).isEqualTo(2);

		this.sut.deleteAssistantById(meetingAssistantsIds.get(0));

		meetingAssistantsIds = this.sut.getAssistantsMeeting(meetingId);
		Assertions.assertThat(meetingAssistantsIds.size()).isEqualTo(1);

		this.sut.deleteAssistantById(meetingAssistantsIds.get(0));

		meetingAssistantsIds = this.sut.getAssistantsMeeting(meetingId);
		Assertions.assertThat(meetingAssistantsIds).isEmpty();

	}

    @ParameterizedTest
	@CsvSource({
		"1, admin1, 1",
		"1, owner1, 2",
		"2, admin1, 3"
	})
	void shouldfindMeetingAssistantByUsernameAndMeetingId(final int meetingId, final String username, int meetingAssitantId) {

		Optional<Integer> meetingAssistantsId = this.sut.findMeetingAssistantByUsernameAndMeetingId(meetingId, username);
		Assertions.assertThat(meetingAssistantsId.get()).isEqualTo(meetingAssitantId);

	}

	
	@ParameterizedTest
	@CsvSource({
		"1",
		"2"
	})
	void comprobarAforo(int meetingId) {
		Meeting meeting = this.meetingService.findMeetingById(meetingId);
		Boolean comprobarAforo = this.sut.checkAforo(meeting);
		Assertions.assertThat(comprobarAforo).isFalse();
	}
	
	@ParameterizedTest
	@CsvSource({
		"5"
	})
	void comprobarAforoWithErrors(int meetingId) {
		Meeting meeting = this.meetingService.findMeetingById(meetingId);
		Boolean comprobarAforo = this.sut.checkAforo(meeting);
		Assertions.assertThat(comprobarAforo).isTrue();
	}
	
	@ParameterizedTest
	@CsvSource({
		"1",
		"2"
	})
	void comprobarReunionFinalizada(int meetingId) {
		Meeting meeting = this.meetingService.findMeetingById(meetingId);
		Boolean comprobarReunionFinalizada = this.sut.checkReunionYaFinalizada(meeting);
		Assertions.assertThat(comprobarReunionFinalizada).isFalse();
	}
	
	@ParameterizedTest
	@CsvSource({
		"5"
	})
	void comprobarReunionFinalizadaWithErrors(int meetingId) {
		Meeting meeting = this.meetingService.findMeetingById(meetingId);
		Boolean comprobarReunionFinalizada = this.sut.checkReunionYaFinalizada(meeting);
		Assertions.assertThat(comprobarReunionFinalizada).isTrue();
	}
	
	@ParameterizedTest
	@CsvSource({
		"4,admin1"
	})
	void comprobarFecha(int meetingId, String username) {
		Meeting meeting = this.meetingService.findMeetingById(meetingId);
		Boolean checkFecha = this.sut.checkFecha(username, meeting);
		Assertions.assertThat(checkFecha).isFalse();
	}
	@ParameterizedTest
	@CsvSource({
		"admin1,1"
	})
	void comprobarFechaWithErrorPorFuera(String username, int bookId) throws DataAccessException, NotVerifiedBookMeetingException {
		Meeting meeting = new Meeting();
		Book book = this.bookService.findBookById(bookId);
		LocalDateTime start = LocalDateTime.of(2020,06,30,12,30,00);
		LocalDateTime end = LocalDateTime.of(2020,06,30,13,30,00);
		meeting.setBook(book);
		meeting.setName("name");
		meeting.setPlace("place");
		meeting.setStart(start);
		meeting.setEnd(end);
		meeting.setCapacity(20);
		Boolean checkFechaPorFuera = this.sut.checkFecha(username, meeting);
		Assertions.assertThat(checkFechaPorFuera).isTrue();
	}
	
	@ParameterizedTest
	@CsvSource({
		"admin1,1"
	})
	void comprobarFechaWithErrorCoincidePrincipio(String username, int bookId) throws DataAccessException, NotVerifiedBookMeetingException {
		Meeting meeting = new Meeting();
		Book book = this.bookService.findBookById(bookId);
		LocalDateTime start = LocalDateTime.of(2020,06,30,12,30,00);
		LocalDateTime end = LocalDateTime.of(2020,06,30,18,30,00);
		meeting.setBook(book);
		meeting.setName("name");
		meeting.setPlace("place");
		meeting.setStart(start);
		meeting.setEnd(end);
		meeting.setCapacity(20);
		Boolean checkCoincidePrincipio = this.sut.checkFecha(username, meeting);
		Assertions.assertThat(checkCoincidePrincipio).isTrue();
	}
	
	@ParameterizedTest
	@CsvSource({
		"admin1,1"
	})
	void comprobarFechaWithErrorCoincideFin(String username, int bookId) throws DataAccessException, NotVerifiedBookMeetingException {;
		Meeting meeting = new Meeting();
		Book book = this.bookService.findBookById(bookId);
		LocalDateTime start = LocalDateTime.of(2020,06,30,10,30,00);
		LocalDateTime end = LocalDateTime.of(2020,06,30,13,30,00);
		meeting.setBook(book);
		meeting.setName("name");
		meeting.setPlace("place");
		meeting.setStart(start);
		meeting.setEnd(end);
		meeting.setCapacity(20);
		Boolean checkCoincideFin = this.sut.checkFecha(username, meeting);
		Assertions.assertThat(checkCoincideFin).isTrue();
	}
	
	@ParameterizedTest
	@CsvSource({
		"admin1,1"
	})
	void comprobarFechaWithErrorPorDentro(String username, int bookId) throws DataAccessException, NotVerifiedBookMeetingException {
		Meeting meeting = new Meeting();
		Book book = this.bookService.findBookById(bookId);
		LocalDateTime start = LocalDateTime.of(2020,06,30,10,30,00);
		LocalDateTime end = LocalDateTime.of(2020,06,30,15,30,00);
		meeting.setBook(book);
		meeting.setName("name");
		meeting.setPlace("place");
		meeting.setStart(start);
		meeting.setEnd(end);
		meeting.setCapacity(20);
		Boolean checkPorDentro = this.sut.checkFecha(username, meeting);
		Assertions.assertThat(checkPorDentro).isTrue();
	}
	
	@ParameterizedTest
	@CsvSource({
		"admin1,1"
	})
	void comprobarFechaWithErrorPorDentro2(String username, int bookId) throws DataAccessException, NotVerifiedBookMeetingException {
		Meeting meeting = new Meeting();
		Book book = this.bookService.findBookById(bookId);
		LocalDateTime start = LocalDateTime.of(2020,06,30,12,00,00);
		LocalDateTime end = LocalDateTime.of(2020,06,30,14,00,00);
		meeting.setBook(book);
		meeting.setName("name");
		meeting.setPlace("place");
		meeting.setStart(start);
		meeting.setEnd(end);
		meeting.setCapacity(20);
		Boolean checkPorDentro = this.sut.checkFecha(username, meeting);
		Assertions.assertThat(checkPorDentro).isTrue();
	}
	
	
	@ParameterizedTest
	@CsvSource({
		"4,admin1,10",
		"3,vet1,2"	
	})
	void shouldCanInscribe(int meetingId, String username, int bookId) {
		Boolean canInscribe = this.sut.canInscribe(meetingId, username, bookId);
		Assertions.assertThat(canInscribe).isTrue();
	}
	
	
	@ParameterizedTest
	@CsvSource({
		"5,admin1,2",
		"5,vet1,8"	
	})
	void shouldCanInscribeWithErrors(int meetingId, String username, int bookId) {
		//La reunion ya ha finalizado y aforo maximo
		Boolean canInscribe = this.sut.canInscribe(meetingId, username, bookId);
		Assertions.assertThat(canInscribe).isFalse();
	}
	
	@ParameterizedTest
	@CsvSource({
		"4,admin1,18",
		"2,vet1,19"
	})
	void shouldInscribeMeeting(int meetingId, String username, int futureId) {
		Meeting meeting = this.meetingService.findMeetingById(meetingId);
		User user = this.userService.findUserByUsername(username);
		MeetingAssistant ma = new MeetingAssistant();
		ma.setMeeting(meeting);
		ma.setUser(user);
		try {
			this.sut.save(ma);
		}catch(CantInscribeMeetingException e) {
			
		}
		//Le toca el 9
		MeetingAssistant maSaved = this.sut.findById(futureId);
		Assertions.assertThat(maSaved.getMeeting()).isEqualTo(meeting);
		Assertions.assertThat(maSaved.getUser()).isEqualTo(user);
		
	}
	
	@ParameterizedTest
	@CsvSource({
		"1,vet1",
		"5,owner1"
	})
	void shouldInscribeMeetingWithErrors(int meetingId, String username) {
		Meeting meeting = this.meetingService.findMeetingById(meetingId);
		User user = this.userService.findUserByUsername(username);
		MeetingAssistant ma = new MeetingAssistant();
		ma.setMeeting(meeting);
		ma.setUser(user);
		//El usuario vet1 no ha leido el libro de la meeting 1
		
		assertThrows(CantInscribeMeetingException.class, ()-> this.sut.save(ma));
		
	}
	
	
	
	
	

	
}
