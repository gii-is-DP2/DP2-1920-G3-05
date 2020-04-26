
package org.springframework.samples.petclinic.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.transaction.annotation.Transactional;

public interface MeetingRepository {

	@Transactional(readOnly = true)
	List<Integer> getMeetingsFromBook(int bookId) throws DataAccessException;

	@Transactional
	@Modifying
	void deleteMeetingById(int meetingId) throws DataAccessException;

	@Transactional(readOnly = true)
	Boolean existsById(int meetingId) throws DataAccessException;

	@Transactional
	@Modifying
	Meeting save(Meeting meeting) throws DataAccessException;

	@Transactional(readOnly = true)
	List<Meeting> findAll() throws DataAccessException;

	@Transactional
	Meeting findById(int id) throws DataAccessException;

	@Transactional
	Collection<Meeting> findBookByNamePlaceBookTile(String name) throws DataAccessException;

	@Transactional
	Integer numberOfMeetings(LocalDateTime time) throws DataAccessException;

	@Transactional
	Object[] meetingsByDay(LocalDateTime time) throws DataAccessException;

}
