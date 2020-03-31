
package org.springframework.samples.petclinic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface MeetingAssistantRepository {

	@Transactional(readOnly = true)
	List<Integer> getAssistantsMeeting(int meetingId) throws DataAccessException;

	@Transactional
	@Modifying
	void deleteAssistantById(int assistantId) throws DataAccessException;

	@Transactional(readOnly = true)
	Boolean existsById(int assistantId) throws DataAccessException;
	@Transactional(readOnly = true)
	Optional<Integer> findMeetingAssistantByUsernameAndMeetingId(int meetingId, String username) throws DataAccessException;;
}
