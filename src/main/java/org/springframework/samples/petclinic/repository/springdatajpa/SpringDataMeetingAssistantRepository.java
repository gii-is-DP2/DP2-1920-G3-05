
package org.springframework.samples.petclinic.repository.springdatajpa;


import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.samples.petclinic.model.MeetingAssistant;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.MeetingAssistantRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SpringDataMeetingAssistantRepository extends MeetingAssistantRepository, CrudRepository<MeetingAssistant, Integer> {

	@Override
	@Transactional
	@Query("SELECT meetingAssistant.id FROM MeetingAssistant meetingAssistant WHERE meetingAssistant.meeting.id = ?1")
	List<Integer> getAssistantsMeeting(int meetingId);

	@Override
	@Transactional
	@Modifying
	@Query("DELETE FROM MeetingAssistant WHERE id = ?1")
	void deleteAssistantById(int assistantId);
	
	@Override
	@Query("SELECT DISTINCT m from Meeting m WHERE m.id in (SELECT meetingAssistant.meeting.id FROM MeetingAssistant meetingAssistant WHERE meetingAssistant.user.username = ?1 )")
	List<Meeting> getMeetingUser (String userId);
	
	@Override
	@Transactional(readOnly = true)
	@Query("SELECT meetingAssistant.id FROM MeetingAssistant meetingAssistant WHERE meetingAssistant.meeting.id = ?1 AND meetingAssistant.user.username=?2")
	Optional<Integer> findMeetingAssistantByUsernameAndMeetingId(int meetingId, String username) throws DataAccessException;
	
	@Override
	@Transactional
	@Query("SELECT COUNT(*) FROM MeetingAssistant meetingAs WHERE (MONTH(meetingAs.meeting.start) = (MONTH(NOW())-1) AND YEAR(meetingAs.meeting.start) = (YEAR(NOW()))) ")
	Integer numberOfMeetingsAssistant() throws DataAccessException;
	
	@Override
	@Transactional
	@Query("SELECT meetingAs.meeting.book.genre,COUNT(*) FROM MeetingAssistant meetingAs WHERE(MONTH(meetingAs.meeting.start) = (MONTH(NOW())-1) AND YEAR(meetingAs.meeting.start) = (YEAR(NOW()))) group by meetingAs.meeting.book.genre")
	Object[] assistantByGenre() throws DataAccessException;
	

	@Override
	@Transactional
	@Query("SELECT meetingAs.meeting.name,meetingAs.meeting.book.title,meetingAs.meeting.place,DAY(meetingAs.meeting.start),COUNT(*) FROM MeetingAssistant meetingAs WHERE (MONTH(meetingAs.meeting.start) = (MONTH(NOW())-1) AND YEAR(meetingAs.meeting.start) = (YEAR(NOW()))) group by meetingAs.meeting")
	Object[] assistantByMeeting() throws DataAccessException;
	
	@Override
	@Transactional
	@Query("SELECT DISTINCT meetingAs.user FROM MeetingAssistant meetingAs WHERE (MONTH(meetingAs.meeting.start) = (MONTH(NOW())-1) AND YEAR(meetingAs.meeting.start) = (YEAR(NOW())))")
	List<User> usersAssisted() throws DataAccessException;
}
