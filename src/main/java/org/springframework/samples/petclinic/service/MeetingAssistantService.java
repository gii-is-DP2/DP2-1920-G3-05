
package org.springframework.samples.petclinic.service;

import java.util.List;
import java.util.Optional;

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


	@Transactional(readOnly = true)
	public List<Integer> getAssistantsMeeting(final int meetingId) throws DataAccessException {
		return this.meetingAssistantRepository.getAssistantsMeeting(meetingId);
	}

	@Transactional
	@Modifying
	public void deleteAssistantById(final int assistantId) throws DataAccessException {
		this.meetingAssistantRepository.deleteAssistantById(assistantId);
	}

	@Transactional(readOnly = true)
	public Boolean existsAssistantById(final int assistantId) throws DataAccessException {
		return this.meetingAssistantRepository.existsById(assistantId);
	}
	@Transactional(readOnly = true)
	public Optional<Integer> findMeetingAssistantByUsernameAndMeetingId(final int meetingId, final String username) {

		return this.meetingAssistantRepository.findMeetingAssistantByUsernameAndMeetingId(meetingId, username);
	}

}
