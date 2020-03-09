package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.repository.MeetingAssistantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeetingAssistantService {

	@Autowired
	private MeetingAssistantRepository meetingAssistantRepository;
	
	/*@Transactional
	public int assistantsCount() {
		return (int)meetingAssistantRepository.count();
	}*/
	
	@Transactional(readOnly = true)
	public List<Integer> getAssistantsMeeting(int meetingId) throws DataAccessException {
		return this.meetingAssistantRepository.getAssistantsMeeting(meetingId);
	}
	
	@Transactional
	@Modifying
	public void deleteAssistantById(int assistantId) throws DataAccessException {
		this.meetingAssistantRepository.deleteAssistantById(assistantId);
	}
	
	@Transactional(readOnly = true)
	public Boolean existsAssistantById(int assistantId) throws DataAccessException {
		return this.meetingAssistantRepository.existsById(assistantId);
	}
	
}
