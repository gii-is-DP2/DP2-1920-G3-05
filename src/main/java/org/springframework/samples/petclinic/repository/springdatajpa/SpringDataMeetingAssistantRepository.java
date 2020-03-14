package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.MeetingAssistant;
import org.springframework.samples.petclinic.repository.MeetingAssistantRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SpringDataMeetingAssistantRepository extends MeetingAssistantRepository, CrudRepository<MeetingAssistant, Integer>{

	@Override
	@Transactional
	@Query("SELECT meetingAssistant.id FROM MeetingAssistant meetingAssistant WHERE meetingAssistant.meeting.id = ?1")
	public List<Integer> getAssistantsMeeting(int meetingId);
	
	@Override
	@Transactional
	@Modifying
	@Query("DELETE FROM MeetingAssistant WHERE id = ?1")
	public void deleteAssistantById(int assistantId);
	
}
