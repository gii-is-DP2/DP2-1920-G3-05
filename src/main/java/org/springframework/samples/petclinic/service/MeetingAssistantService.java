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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeetingAssistantService {

	@Autowired
	private MeetingAssistantRepository meetingAssistantRepository;
	private MeetingRepository 		   meetingRepository;
		
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
	public void save(final MeetingAssistant meetingAssistant) throws DataAccessException {
		this.meetingAssistantRepository.save(meetingAssistant);
		
	}
	
	@Transactional
	public MeetingAssistant findById(int meetingid) throws DataAccessException {
		return this.findById(meetingid);
	}
	
	
	
	public Boolean checkAforo(int meetingId) throws DataAccessException {
		Boolean espacioLibre = false;
		List<Integer> integrantes = this.getAssistantsMeeting(meetingId);
		Integer numeroIntegrantes = integrantes.size();
		Meeting reunion = this.meetingRepository.findById(meetingId);
		Integer aforoReunion = reunion.getCapacity();
		if(numeroIntegrantes==aforoReunion) {
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
	
	public Boolean checkFecha(String meetingUserId,int meetingId) {
		List<Meeting> reuniones = this.meetingAssistantRepository.getMeetingUser(meetingUserId);
		Meeting meeting = this.meetingRepository.findById(meetingId);
		Boolean solape = false;
		Boolean anterior = false;
		Boolean estaContenida = false;
		LocalDateTime fecha = meeting.getStart();
		for (Meeting meeting2 : reuniones) {
			if (!meeting2.equals(meeting)) {
				if (meeting2.getEnd().isAfter(fecha)) {
					anterior = meeting.getEnd().isBefore(meeting2.getStart()) || meeting.getEnd().isEqual(meeting2.getStart());
					solape = !anterior && fecha.isBefore(meeting2.getStart()) && (meeting.getEnd().isAfter(meeting2.getStart()) || !meeting.getEnd().isEqual(meeting2.getStart())) || fecha.isEqual(meeting.getStart());
					estaContenida = fecha.isAfter(meeting2.getStart()) && fecha.isBefore(meeting2.getEnd());

					if (solape || estaContenida) {
						break;
					}
				}
			}
		}
		
		return solape||estaContenida;
		
	
	
	}
	
	
}
