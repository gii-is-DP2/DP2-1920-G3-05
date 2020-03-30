package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.logging.log4j.util.Strings;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class MeetingValidator implements Validator{


	@Override
	public boolean supports(Class<?> clazz) {
		return Meeting.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Meeting meeting = (Meeting) target;
		
		if(Strings.isBlank(meeting.getName())) {
			errors.rejectValue("name", "Must not be empty", "Must not be empty");
		}else if(meeting.getName().length()<3) {
			errors.rejectValue("name", "Must have at least 3 characters", "Must have at least 3 characters");
		}

		if(Strings.isBlank(meeting.getPlace())) {
			errors.rejectValue("place", "Must not be empty", "Must not be empty");
		}
		
		if(meeting.getCapacity() == null) {
			errors.rejectValue("capacity", "Must not be empty", "Must not be empty");
		}else if(meeting.getCapacity() < 5){
			errors.rejectValue("capacity", "Must be at least 5", "Must be at least 5");
		}
		
		if(meeting.getStart()==null) {
			errors.rejectValue("start", "Must not be empty", "Must not be empty");
		}

		if(meeting.getEnd()==null) {
			errors.rejectValue("end", "Must not be empty", "Must not be empty");
		}

		if(meeting.getStart()!=null && meeting.getEnd()!=null && meeting.getEnd().isBefore(meeting.getStart())) {
			errors.rejectValue("end", "End date must be after start date", "End date must be after start date"); 
		}

		if(meeting.getStart()!=null && meeting.getEnd()!=null && meeting.getStart().isBefore(LocalDateTime.now().plusDays(3))) {
			errors.rejectValue("start", "Meeting must be planned at least 3 days in advanced", "Meeting must be planned at least 3 days in advanced"); 
		}

		if(meeting.getStart()!=null && meeting.getEnd()!=null) {
			LocalDateTime begin = meeting.getStart();
			LocalDateTime end = meeting.getEnd();
			Long minutes = ChronoUnit.MINUTES.between(begin, end);
			if(minutes<60){
			errors.rejectValue("end", "Meeting must last at least 1 hour", "Meeting must last at least 1 hour"); 
			}
		}
	}

	
}
