
package org.springframework.samples.petclinic.service;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class MeetingAssistantServiceTest {

	@Autowired
	private MeetingAssistantService sut;
	
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

}
