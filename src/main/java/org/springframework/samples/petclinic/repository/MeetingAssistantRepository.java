
package org.springframework.samples.petclinic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.samples.petclinic.model.MeetingAssistant;
import org.springframework.samples.petclinic.model.User;
import org.springframework.transaction.annotation.Transactional;

public interface MeetingAssistantRepository {

	@Transactional(readOnly = true)
	List<Integer> getAssistantsMeeting(int meetingId) throws DataAccessException;
	
	@Transactional(readOnly = true)
	List<MeetingAssistant> getAssistantsOfMeeting(int meetingId);

	@Transactional
	@Modifying
	void deleteAssistantById(int assistantId) throws DataAccessException;

	@Transactional(readOnly = true)
	Boolean existsById(int assistantId) throws DataAccessException;
	@Transactional(readOnly = true)
	Optional<Integer> findMeetingAssistantByUsernameAndMeetingId(int meetingId, String username) throws DataAccessException;;
	
	@Modifying
	MeetingAssistant save(MeetingAssistant meetingAssistant) throws DataAccessException;
	
	MeetingAssistant findById(int id) throws DataAccessException;

	
	List<Meeting> getMeetingUser(String userId);

	@Transactional
	Integer numberOfMeetingsAssistant() throws DataAccessException;
	
	@Transactional
	Object[] assistantByGenre() throws DataAccessException;
	

	@Transactional
	Object[] assistantByMeeting() throws DataAccessException;

	@Transactional
	List<User> usersAssisted() throws DataAccessException;

	
}
