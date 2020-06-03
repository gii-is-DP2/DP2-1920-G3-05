
package org.springframework.samples.petclinic.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.transaction.annotation.Transactional;

public interface MeetingRepository {

	@Transactional(readOnly = true)
	List<Integer> getMeetingsFromBook(int bookId) ;

	@Transactional
	@Modifying
	void deleteMeetingById(int meetingId) ;

	@Transactional(readOnly = true)
	Boolean existsById(int meetingId) ;

	@Transactional
	@Modifying
	Meeting save(Meeting meeting) ;

	@Transactional(readOnly = true)
	List<Meeting> findAll() ;

	@Transactional
	Meeting findById(int id) ;

	@Transactional
	Collection<Meeting> findBookByNamePlaceBookTile(String name) ;

	@Transactional
	Integer numberOfMeetings(LocalDateTime time) ;

	@Transactional
	Object[][] meetingsByDay(LocalDateTime time) ;

}
