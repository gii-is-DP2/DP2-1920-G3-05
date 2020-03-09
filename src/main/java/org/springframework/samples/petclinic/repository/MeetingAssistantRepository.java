package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.MeetingAssistant;
import org.springframework.transaction.annotation.Transactional;

public interface MeetingAssistantRepository {

	@Transactional(readOnly = true)
	List<Integer> getAssistantsMeeting(int meetingId) throws DataAccessException; 
	
	@Transactional
	@Modifying
	void deleteAssistantById(int assistantId) throws DataAccessException;
	
	@Transactional(readOnly = true)
	Boolean existsById(int assistantId) throws DataAccessException;
}
