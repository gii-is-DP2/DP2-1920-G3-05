package org.springframework.samples.petclinic.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.repository.MeetingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeetingService {

	@Autowired
	private MeetingRepository meetingRepository;
	
	@Autowired 
	private MeetingAssistantService meetingAssistantService;
		
	@Transactional(readOnly = true)
	public List<Integer> getMeetingsFromBook(int bookId) throws DataAccessException {
		return this.meetingRepository.getMeetingsFromBook(bookId);
	}
	
	@Transactional
	@Modifying
	public void deleteMeeting(int meetingId) throws DataAccessException {
		//Primero hay que borrar los asistentes a la reunion
		List<Integer> meetingAssistantsId = this.meetingAssistantService.getAssistantsMeeting(meetingId);
		if(meetingAssistantsId!=null && !meetingAssistantsId.isEmpty()) {
			for(Integer i: meetingAssistantsId) {
				this.meetingAssistantService.deleteAssistantById(i);
			}
		}
		this.meetingRepository.deleteMeetingById(meetingId);
	}
	
	@Transactional(readOnly = true)
	public Boolean existsMeetingById(int meetingId) throws DataAccessException {
		return this.meetingRepository.existsById(meetingId);
	}
}
