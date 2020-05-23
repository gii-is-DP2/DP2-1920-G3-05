
package org.springframework.samples.petclinic.integration.DB;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.samples.petclinic.repository.MeetingRepository;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("mysql")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MeetingQueriesIntegrationTests {

	@Autowired
	private MeetingRepository sut;


	@ParameterizedTest
	@CsvSource({
		"1, 2", 
		"7, 1", 
		"3, 0"
	})
	public void shouldGetMeetingsFromBook(final int bookId, final int numMeetings) {
		List<Integer> meetingIds = this.sut.getMeetingsFromBook(bookId);

		Assertions.assertThat(meetingIds.size()).isEqualTo(numMeetings);

	}
	@ParameterizedTest
	@CsvSource({
		"primera, 1", "reunion, 4"
	})
	public void shouldGetMeetingsFromName(final String name, final int numMeetings) {
		Collection<Meeting> meetings = this.sut.findBookByNamePlaceBookTile(name);

		Assertions.assertThat(meetings.size()).isEqualTo(numMeetings);

	}
	@ParameterizedTest
	@CsvSource({
		"Nowhere, 2", "Biblioteca ETSII, 1", "error, 0"
	})
	public void shouldGetMeetingsFromPlace(final String name, final int numMeetings) {
		Collection<Meeting> meetings = this.sut.findBookByNamePlaceBookTile(name);

		Assertions.assertThat(meetings.size()).isEqualTo(numMeetings);

	}
	@ParameterizedTest
	@CsvSource({
		"2020-04-23T19:47:30.107,9",
		"2020-06-23T19:47:30.107,1"
	})
	public void GetNumberOfMeetings(final LocalDateTime time, final Integer numberMeetingsExpected) {

		Integer numberOfMeetings = this.sut.numberOfMeetings(time);
		Assertions.assertThat(numberOfMeetings).isEqualTo(numberMeetingsExpected);
	}
	@ParameterizedTest
	@CsvSource({
		"20,3",
		"7,1", 
		"21,1"
	})
	public void GetNumberOfMeetingsbyDay(final Integer day, final Long numberMeetingsExpected) {
		LocalDateTime time = LocalDateTime.of(2020, 04, 23, 19, 55);
		Object[][] meetingsbyDay = this.sut.meetingsByDay(time);
		for(Object[] o: meetingsbyDay) {
			if(o[0].equals(day)) {
				Assertions.assertThat(o[1].equals(numberMeetingsExpected));
			}
		}
	}

}
