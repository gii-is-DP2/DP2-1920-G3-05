package org.springframework.samples.petclinic.model;


import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "meeting_assistants")
public class MeetingAssistant extends BaseEntity{

	@ManyToOne(optional = false)
	private User user;
	
	@ManyToOne(optional = false)
	private Meeting meeting;
}
