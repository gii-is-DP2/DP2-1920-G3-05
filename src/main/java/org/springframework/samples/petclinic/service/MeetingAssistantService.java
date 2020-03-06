package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.repository.MeetingAssistantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeetingAssistantService {

	@Autowired
	private MeetingAssistantRepository meetingAssistantRepository;
	
	@Transactional
	public int assistantsCount() {
		return (int)meetingAssistantRepository.count();
	}
	
}
