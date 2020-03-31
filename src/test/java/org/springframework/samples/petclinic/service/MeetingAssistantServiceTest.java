
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


	@Test
	void shouldGetMeetingAssistantsIdsFromMeetingId() {
		int meetingId = 1;
		List<Integer> meetingAssistantsIds = this.sut.getAssistantsMeeting(meetingId);
		Assertions.assertThat(meetingAssistantsIds.size()).isEqualTo(2);
		Assertions.assertThat(meetingAssistantsIds).contains(1, 2);

		meetingId = 3;
		meetingAssistantsIds = this.sut.getAssistantsMeeting(meetingId);
		Assertions.assertThat(meetingAssistantsIds.size()).isEqualTo(2);
		Assertions.assertThat(meetingAssistantsIds).contains(5, 6);

		meetingId = 4;
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

	@Test
	void shouldDeleteMeetingAssistantFromMeeting() {
		int meetingId = 1;
		List<Integer> meetingAssistantsIds = this.sut.getAssistantsMeeting(meetingId);
		Assertions.assertThat(meetingAssistantsIds.size()).isEqualTo(2);

		this.sut.deleteAssistantById(meetingAssistantsIds.get(0));

		meetingAssistantsIds = this.sut.getAssistantsMeeting(meetingId);
		Assertions.assertThat(meetingAssistantsIds.size()).isEqualTo(1);

		this.sut.deleteAssistantById(meetingAssistantsIds.get(0));

		meetingAssistantsIds = this.sut.getAssistantsMeeting(meetingId);
		Assertions.assertThat(meetingAssistantsIds).isEmpty();

	}
}
