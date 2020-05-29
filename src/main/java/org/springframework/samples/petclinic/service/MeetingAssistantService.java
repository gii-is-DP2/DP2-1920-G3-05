
package org.springframework.samples.petclinic.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.samples.petclinic.model.MeetingAssistant;
import org.springframework.samples.petclinic.repository.MeetingAssistantRepository;
import org.springframework.samples.petclinic.repository.MeetingRepository;
import org.springframework.samples.petclinic.service.exceptions.CantInscribeMeetingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeetingAssistantService {

	@Autowired
	private MeetingAssistantRepository meetingAssistantRepository;
	
	@Autowired
	private ReadBookService				readBookService;

	@Autowired
	private MeetingRepository			meetingRepo;
		
	@Transactional(readOnly = true)
	public List<Integer> getAssistantsMeeting(final int meetingId)  {
		return this.meetingAssistantRepository.getAssistantsMeeting(meetingId);
	}
	
	@Transactional(readOnly = true)
	public List<MeetingAssistant> getAssistantsOfMeeting(final int meetingId)  {
		return this.meetingAssistantRepository.getAssistantsOfMeeting(meetingId);
	}
	
	@Transactional(readOnly= true)
	public List<Meeting> getMeetingUser (String userId){
		return this.meetingAssistantRepository.getMeetingUser(userId);
	}
	
	
	@Transactional
	@Modifying
	public void deleteAssistantById(final int assistantId)  {
		this.meetingAssistantRepository.deleteAssistantById(assistantId);
	}

	@Transactional(readOnly = true)
	public Boolean existsAssistantById(final int assistantId)  {
		return this.meetingAssistantRepository.existsById(assistantId);
	}
	@Transactional(readOnly = true)
	public Optional<Integer> findMeetingAssistantByUsernameAndMeetingId(final int meetingId, final String username) {

		return this.meetingAssistantRepository.findMeetingAssistantByUsernameAndMeetingId(meetingId, username);
	}

	
	@Transactional
	public void save(final MeetingAssistant meetingAssistant) throws CantInscribeMeetingException{
		String username = meetingAssistant.getUser().getUsername();
		Meeting meeting = meetingAssistant.getMeeting();
		Boolean CanInscribe = this.canInscribe(meeting.getId(), username, meeting.getBook().getId());
		if(CanInscribe) {
			this.meetingAssistantRepository.save(meetingAssistant);
		}else {
			throw new CantInscribeMeetingException();
		}
		
	}
	
	public MeetingAssistant findById(int meetingAssistantId) {
		return this.meetingAssistantRepository.findById(meetingAssistantId);
	}
	
	
	
	public Boolean checkAforo(Meeting meeting)  {
		Boolean espacioLibre = false;
		List<Integer> integrantes = this.getAssistantsMeeting(meeting.getId());
		Integer numeroIntegrantes = integrantes.size();
		Integer aforoReunion = meeting.getCapacity();
		if(numeroIntegrantes>=aforoReunion) {
			espacioLibre = true;
		}
		return espacioLibre;
		
	}
	
	public Boolean checkReunionYaFinalizada (Meeting meeting) {
		Boolean fechaReunion = false;
		if(meeting.getStart().isBefore(LocalDateTime.now())) {
			fechaReunion = true;
		}
		return fechaReunion;
	}
	
	public Boolean checkFecha(String meetingUserId, Meeting meeting) {
		List<Meeting> reuniones = this.meetingAssistantRepository.getMeetingUser(meetingUserId);
		Boolean porFuera = false;
		Boolean coincideComienzo = false;
		Boolean coincideFin = false;
		Boolean porDentro = false;
		for (Meeting meeting2 : reuniones) {
			porFuera= meeting2.getStart().isBefore(meeting.getStart()) && meeting2.getEnd().isAfter(meeting.getEnd());
			coincideComienzo =  meeting2.getStart().isBefore(meeting.getStart()) && meeting2.getEnd().isAfter(meeting.getStart()); 
			coincideFin =	meeting2.getStart().isBefore(meeting.getEnd()) && meeting2.getEnd().isAfter(meeting.getEnd());
			porDentro = (meeting2.getStart().isAfter(meeting.getStart()) || meeting2.getStart().isEqual(meeting.getStart())) && (meeting2.getEnd().isBefore(meeting.getEnd()) || meeting2.getEnd().isEqual(meeting.getEnd()));
			if(porFuera || coincideComienzo || coincideFin || porDentro) {
				break;
			}
			}
		
		
		 return porFuera || coincideComienzo || coincideFin || porDentro;
		
	
	
	}
	public Boolean canInscribe(int meetingId, String username, int bookId){
		Meeting meeting = this.meetingRepo.findById(meetingId);
		Boolean esLibroLeido = this.readBookService.esReadBook(bookId, username);
		Boolean aforo = checkAforo(meeting);
		Boolean fecha = checkFecha(username, meeting);
		Boolean reunionFinalizada = checkReunionYaFinalizada(meeting);
		
		if(!esLibroLeido || aforo == true || fecha==true || reunionFinalizada==true) {
			return false;
		}else {
			return true;
		}
		
	}
	
	
}
