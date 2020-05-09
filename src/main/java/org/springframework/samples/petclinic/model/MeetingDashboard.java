package org.springframework.samples.petclinic.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class MeetingDashboard implements Serializable{
	
	private static final long	serialVersionUID	= 1L;
	
	Object[] assistantByGenre;
	
	Object[] meetingsByDay;
	
	Object[] assistantByMeeting;
	
	Integer numberOfMeetingsAssistant;
	
	Integer numberOfMeetings;

	Double usersAssisted;

}
