package org.springframework.samples.petclinic.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
	private MeetingRepository 		   meetingRepository;
	@Autowired
	private ReadBookService				readBookService;
		
	@Transactional(readOnly = true)
	public List<Integer> getAssistantsMeeting(int meetingId) throws DataAccessException {
		return this.meetingAssistantRepository.getAssistantsMeeting(meetingId);
	}
	
	@Transactional(readOnly= true)
	public List<Meeting> getMeetingUser (String userId){
		return this.meetingAssistantRepository.getMeetingUser(userId);
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
	
	
	
	public Boolean checkAforo(int meetingId) throws DataAccessException {
		Boolean espacioLibre = false;
		List<Integer> integrantes = this.getAssistantsMeeting(meetingId);
		Integer numeroIntegrantes = integrantes.size();
		Meeting reunion = this.meetingRepository.findById(meetingId);
		Integer aforoReunion = reunion.getCapacity();
		if(numeroIntegrantes>=aforoReunion) {
			espacioLibre = true;
		}
		return espacioLibre;
		
	}
	
	public Boolean checkReunionYaFinalizada (int meetingId) throws DataAccessException{
		Boolean fechaReunion = false;
		Meeting reunion = this.meetingRepository.findById(meetingId);
		if(reunion.getStart().isBefore(LocalDateTime.now())) {
			fechaReunion = true;
		}
		return fechaReunion;
	}
	
	public Boolean checkFecha(String meetingUserId, int meetingId) {
		List<Meeting> reuniones = this.meetingAssistantRepository.getMeetingUser(meetingUserId);
		Meeting meeting = this.meetingRepository.findById(meetingId);
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
		
		Boolean esLibroLeido = this.readBookService.esReadBook(bookId, username);
		Boolean aforo = checkAforo(meetingId);
		Boolean fecha = checkFecha(username, meetingId);
		Boolean reunionFinalizada = checkReunionYaFinalizada(meetingId);
		
		if(!esLibroLeido || aforo == true || fecha==true || reunionFinalizada==true) {
			return false;
		}else {
			return true;
		}
		
	}
	
	
}
