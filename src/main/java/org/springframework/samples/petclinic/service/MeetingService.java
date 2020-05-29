
package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.samples.petclinic.repository.MeetingRepository;
import org.springframework.samples.petclinic.service.exceptions.NotVerifiedBookMeetingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeetingService {

	private MeetingRepository		meetingRepository;

	private MeetingAssistantService	meetingAssistantService;


	@Autowired
	public MeetingService(MeetingRepository meetingRepository, MeetingAssistantService meetingAssistantService, final BookService bookService) {
		this.meetingRepository = meetingRepository;
		this.meetingAssistantService = meetingAssistantService;
	}

	@Transactional(readOnly = true)
	public List<Integer> getMeetingsIdFromBook(final int bookId)  {
		return this.meetingRepository.getMeetingsFromBook(bookId);
	}

	@Transactional(readOnly = true)
	public List<Meeting> findAllMeetings()  {
		return this.meetingRepository.findAll();
	}

	@Transactional
	@Modifying
	public void deleteMeeting(final int meetingId)  {
		//Primero hay que borrar los asistentes a la reunion
		List<Integer> meetingAssistantsId = this.meetingAssistantService.getAssistantsMeeting(meetingId);
		if (meetingAssistantsId != null && !meetingAssistantsId.isEmpty()) {
			for (Integer i : meetingAssistantsId) {
				this.meetingAssistantService.deleteAssistantById(i);
			}
		}
		this.meetingRepository.deleteMeetingById(meetingId);
	}

	@Transactional(readOnly = true)
	public Boolean existsMeetingById(final int meetingId)  {
		return this.meetingRepository.existsById(meetingId);
	}

	@Transactional
	@Modifying
	public void addMeeting(final Meeting meeting) throws NotVerifiedBookMeetingException {
		Boolean isVerifiedBook = meeting.getBook().getVerified();
		if (Boolean.TRUE.equals(isVerifiedBook)) {
			this.meetingRepository.save(meeting);
		} else {
			throw new NotVerifiedBookMeetingException();
		}
	}

	@Transactional
	public Meeting findMeetingById(final int meetingid)  {
		return this.meetingRepository.findById(meetingid);
	}

	@Transactional
	public Collection<Meeting> findMeetingsByNamePlaceBookTile(String name)  {
		return this.meetingRepository.findBookByNamePlaceBookTile(name.toUpperCase());
	}
}
