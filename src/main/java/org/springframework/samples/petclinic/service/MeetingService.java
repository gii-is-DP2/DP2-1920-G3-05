package org.springframework.samples.petclinic.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.samples.petclinic.repository.MeetingRepository;
import org.springframework.samples.petclinic.service.exceptions.NotVerifiedBookMeetingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeetingService {

	private MeetingRepository meetingRepository;
	
	private MeetingAssistantService meetingAssistantService;


	@Autowired
	public MeetingService(MeetingRepository meetingRepository, MeetingAssistantService meetingAssistantService, BookService bookService) {
		this.meetingRepository = meetingRepository;
		this.meetingAssistantService = meetingAssistantService;
	}
		
	@Transactional(readOnly = true)
	public List<Integer> getMeetingsIdFromBook(int bookId) throws DataAccessException {
		return this.meetingRepository.getMeetingsFromBook(bookId);
	}

	@Transactional(readOnly = true)
	public List<Meeting> findAllMeetings() throws DataAccessException {
		return this.meetingRepository.findAll();
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

	@Transactional
	@Modifying
	public void addMeeting(Meeting meeting) throws DataAccessException, NotVerifiedBookMeetingException {
		Boolean isVerifiedBook = meeting.getBook().getVerified();
		if(isVerifiedBook){
			this.meetingRepository.save(meeting);
		}else{
			throw new NotVerifiedBookMeetingException();
		}
	}

	@Transactional
	public Meeting findMeetingById(int meetingid) throws DataAccessException {
		return this.meetingRepository.findById(meetingid);
	}
}
