
package org.springframework.samples.petclinic.integration.DB;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.samples.petclinic.model.MeetingAssistant;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.MeetingAssistantRepository;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("mysql")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MeetingAssistantQueriesIntegrationTests {

	@Autowired
	private MeetingAssistantRepository sut;


	@ParameterizedTest
	@CsvSource({
		"1,2", "3,2", "4,0"
	})
	void shouldGetMeetingAssistantsIdsFromMeetingId(final int meetingId, final int results) {
		List<Integer> meetingAssistantsIds = this.sut.getAssistantsMeeting(meetingId);
		Assertions.assertThat(meetingAssistantsIds.size()).isEqualTo(results);
	}
	@ParameterizedTest
	@CsvSource({
		"1,2", "3,2", "4,0"
	})
	void shouldGetMeetingAssistantsFromMeetingId(final int meetingId, final int results) {
		List<MeetingAssistant> meetingAssistants = this.sut.getAssistantsOfMeeting(meetingId);
		Assertions.assertThat(meetingAssistants.size()).isEqualTo(results);
	}
	@ParameterizedTest
	@CsvSource({
		"1, admin1, 1", "1, owner1, 2", "2, admin1, 3"
	})
	void shouldfindMeetingAssistantByUsernameAndMeetingId(final int meetingId, final String username, final int meetingAssitantId) {

		Optional<Integer> meetingAssistantsId = this.sut.findMeetingAssistantByUsernameAndMeetingId(meetingId, username);
		Assertions.assertThat(meetingAssistantsId.get()).isEqualTo(meetingAssitantId);

	}
	@ParameterizedTest
	@CsvSource({
		"admin1, 6", "owner1, 6"
	})
	void shouldGetMeetingsByUserName(final String user, final Integer res) {

		List<Meeting> meetings = this.sut.getMeetingUser(user);
		Assertions.assertThat(meetings.size()).isEqualTo(res);

	}
	@ParameterizedTest
	@CsvSource({
		"2020-04-23T19:47:30.107,10", "2020-11-23T19:47:30.107,4"
	})
	void shouldGetNumberOfMeetingsAssistantInAMonth(final LocalDateTime time, final Integer res) {

		Integer asistentes = this.sut.numberOfMeetingsAssistant(time);
		Assertions.assertThat(asistentes).isEqualTo(res);

	}
	@ParameterizedTest
	@CsvSource({
		"Romance,2", "Fiction, 4", "Historical, 1"
	})
	void GetAssitantsbyGenre(final String nameGenre, final Long numeroAsistentes) {
		LocalDateTime time = LocalDateTime.of(2020, 04, 23, 19, 55);
		Object[][] AssitantsByGenre = this.sut.assistantByGenre(time);
		for (Object[] o : AssitantsByGenre) {
			if (o[0].equals(nameGenre)) {
				Assertions.assertThat(o[1].equals(numeroAsistentes));
			}
		}
	}
	@ParameterizedTest
	@CsvSource({
		"0, Reunion prueba aforo, 2", "2, Club de lectura, 2", "3, Club de lectura, 2"
	})
	void GetAssitantsbyMeeting(final Integer numbObject, final String nameMeeting, final Long numeroAsistentes) {
		LocalDateTime time = LocalDateTime.of(2020, 04, 23, 19, 55);
		Object[] AssitantsByGenre = this.sut.assistantByMeeting(time);
		Object[] aux = (Object[]) AssitantsByGenre[numbObject];

		Assertions.assertThat(aux[0]).isEqualTo(nameMeeting);
		Assertions.assertThat(aux[aux.length - 1]).isEqualTo(numeroAsistentes);

	}
	@ParameterizedTest
	@CsvSource({
		"2020-04-23T19:47:30.107,4", "2020-07-23T19:47:30.107,2"
	})
	void GetUsersAssisteds(final LocalDateTime time, final Integer usersExpected) {

		List<User> users = this.sut.usersAssisted(time);

		Assertions.assertThat(users.size()).isEqualTo(usersExpected);

	}
}
