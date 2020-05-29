
package org.springframework.samples.petclinic.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.samples.petclinic.model.MeetingAssistant;
import org.springframework.samples.petclinic.model.User;
import org.springframework.transaction.annotation.Transactional;

public interface MeetingAssistantRepository {

	@Transactional(readOnly = true)
	List<Integer> getAssistantsMeeting(int meetingId) ;

	@Transactional(readOnly = true)
	List<MeetingAssistant> getAssistantsOfMeeting(int meetingId);

	@Transactional
	@Modifying
	void deleteAssistantById(int assistantId) ;

	@Transactional(readOnly = true)
	Boolean existsById(int assistantId) ;
	@Transactional(readOnly = true)
	Optional<Integer> findMeetingAssistantByUsernameAndMeetingId(int meetingId, String username) ;

	@Modifying
	MeetingAssistant save(MeetingAssistant meetingAssistant) ;

	MeetingAssistant findById(int id) ;

	List<Meeting> getMeetingUser(String userId);

	@Transactional
	Integer numberOfMeetingsAssistant(LocalDateTime time) ;

	@Transactional
	Object[][] assistantByGenre(LocalDateTime time) ;

	@Transactional
	Object[] assistantByMeeting(LocalDateTime time) ;

	@Transactional
	List<User> usersAssisted(LocalDateTime time) ;

}
