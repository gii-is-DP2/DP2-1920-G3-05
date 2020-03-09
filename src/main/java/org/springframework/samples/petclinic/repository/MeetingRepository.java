package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.transaction.annotation.Transactional;

public interface MeetingRepository{

	@Transactional(readOnly = true)
	List<Integer> getMeetingsFromBook(int bookId) throws DataAccessException;
	
	@Transactional
	@Modifying
	void deleteMeetingById(int meetingId) throws DataAccessException;
	
	@Transactional(readOnly = true)
	Boolean existsById(int meetingId) throws DataAccessException;
}
