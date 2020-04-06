package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.samples.petclinic.model.MeetingAssistant;
import org.springframework.samples.petclinic.model.Publication;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.MeetingAssistantService;
import org.springframework.samples.petclinic.service.MeetingService;
import org.springframework.samples.petclinic.service.ReadBookService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class MeetingAssistantController {
	
	@Autowired
	private MeetingAssistantService 	meetingAssistantService;
	
	@Autowired
	private MeetingService				meetingService;
	
	@Autowired
	private ReadBookService				readBookService;
	
	@Autowired
	private UserService					userService;
	
	
	@Autowired
	public MeetingAssistantController(final MeetingAssistantService meetingAssistantService, final MeetingService meetingService,
			final ReadBookService readBookService, final UserService userService) {
		this.meetingAssistantService = meetingAssistantService;
		this.meetingService = meetingService;
		this.readBookService = readBookService;
		this.userService = userService;
	}
	
	@GetMapping(value = "/meetings/{meetingId}/inscribe")
	public String inscribe(final ModelMap modelMap,@PathVariable("meetingId") final int meetingId) {
		String view = "meetings/meetingsList";
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		Meeting meeting = this.meetingService.findMeetingById(meetingId);
		Boolean esLibroLeido = this.readBookService.esReadBook(meeting.getBook().getId(), userDetail.getUsername());
		Boolean aforo = this.meetingAssistantService.checkAforo(meetingId);
		Boolean fecha = this.meetingAssistantService.checkFecha(userDetail.getUsername(), meetingId);
		Boolean reunionFinalizada = this.meetingAssistantService.checkReunionYaFinalizada(meetingId);
		if(!esLibroLeido || aforo || fecha || reunionFinalizada) {
			return "redirect:/oups";
		}
		MeetingAssistant meetingAssistant = new MeetingAssistant();
		meetingAssistant.setMeeting(meeting);
		User user = this.userService.findUserByUsername(userDetail.getUsername());
		meetingAssistant.setUser(user);
		this.meetingAssistantService.save(meetingAssistant);
		modelMap.put("meetingAssistant", meetingAssistant);
		return view;

	}

}
