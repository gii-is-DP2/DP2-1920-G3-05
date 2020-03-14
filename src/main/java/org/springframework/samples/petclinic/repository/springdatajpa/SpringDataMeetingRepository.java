package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.samples.petclinic.repository.MeetingRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SpringDataMeetingRepository extends CrudRepository<Meeting, Integer>, MeetingRepository{

	@Override
	@Transactional(readOnly = true)
	@Query("SELECT meeting.id FROM Meeting meeting WHERE meeting.book.id = ?1")
	public List<Integer> getMeetingsFromBook(int bookId);
	
	@Override
	@Transactional
	@Modifying
	@Query("DELETE FROM Meeting WHERE id = ?1")
	public void deleteMeetingById(int meetingId);
}
