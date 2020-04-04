
package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.samples.petclinic.service.MeetingAssistantService;
import org.springframework.samples.petclinic.service.MeetingService;
import org.springframework.samples.petclinic.service.exceptions.NotVerifiedBookMeetingException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MeetingController {

	private MeetingService			meetingService;
	private MeetingAssistantService	meetingAssistantService;
	private BookService				bookService;


	@Autowired
	public MeetingController(final MeetingService meetingService, final BookService bookService, final MeetingAssistantService meetingAssistantService) {
		this.meetingService = meetingService;
		this.bookService = bookService;
		this.meetingAssistantService = meetingAssistantService;
	}

	@InitBinder("meeting")
	public void initMeetingBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new MeetingValidator());
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("book");
		dataBinder.setValidator(new MeetingValidator());
	}

	@GetMapping(value = "/meetings")
	public String getMeetings(Meeting meeting, final BindingResult result, final ModelMap model) {

		// find books by title
		List<Meeting> meetings = this.meetingService.findAllMeetings();

		if (meetings.isEmpty()) {
			// no meetings found
			//result.rejectValue("title", "notFound", "not found");
			return "meetings/meetingsList";
		} else if (meetings.size() == 1) {
			// 1 meeting found
			meeting = meetings.get(0);
			return "redirect:/meetings/" + meeting.getId();
		} else {
			// multiple meetings found
			model.put("meetings", meetings);
			return "meetings/meetingsList";
		}
	}

	@GetMapping(value = "/meetings/{meetingId}")
	public ModelAndView showMeeting(@PathVariable("meetingId") final int meetingId, final ModelMap modelMap) {
		Meeting meeting = this.meetingService.findMeetingById(meetingId);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userdetails = (UserDetails) auth.getPrincipal();
		Optional<Integer> meetingAssistantId = this.meetingAssistantService.findMeetingAssistantByUsernameAndMeetingId(meetingId, userdetails.getUsername());
		ModelAndView mav = new ModelAndView("meetings/meetingDetails");
		mav.addObject("meeting", meeting);
		if (meetingAssistantId.isPresent()) {
			modelMap.put("suscribed", true);

		}
		return mav;
	}

	@GetMapping(value = "/admin/books/{bookId}/meetings/new")
	public String addMeeting(final ModelMap modelMap, @PathVariable("bookId") final int bookId) {
		String view = "meetings/meetingAdd";
		Meeting meeting = new Meeting();
		Book book = this.bookService.findBookById(bookId);
		if (book.getVerified()) {
			meeting.setBook(book);
			modelMap.addAttribute("meeting", meeting);
			return view;
		} else {
			return "redirect:/oups";
		}

	}

	@PostMapping(value = "/admin/books/{bookId}/meetings/new")
	public String saveMeeting(@Valid final Meeting meeting, final BindingResult result, final ModelMap modelMap, @PathVariable("bookId") final int bookId) {
		Book book = this.bookService.findBookById(bookId);
		meeting.setBook(book);

		if (result.hasErrors()) {
			return "meetings/meetingAdd";
		} else {
			try {
				this.meetingService.addMeeting(meeting);
				return "redirect:/meetings";
			} catch (NotVerifiedBookMeetingException e) {
				return "redirect:/oups";
			}
		}
	}
	@GetMapping(value = "/meetings/{meetingId}/unsuscribe")
	public ModelAndView Unsuscribe(@PathVariable("meetingId") final int meetingId, final ModelMap modelMap) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userdetails = (UserDetails) auth.getPrincipal();
		Meeting meeting = this.meetingService.findMeetingById(meetingId);
		Optional<Integer> meetingAssistantId = this.meetingAssistantService.findMeetingAssistantByUsernameAndMeetingId(meetingId, userdetails.getUsername());

		if (meetingAssistantId.isPresent()) {
			if (!meeting.getEnd().isBefore(LocalDateTime.now())) {
				this.meetingAssistantService.deleteAssistantById(meetingAssistantId.get());
				modelMap.put("mensaje", "You are successfully unsubscribed");
			} else {
				modelMap.put("mensaje", "The meeting has already been held!");
			}
		} else {
			modelMap.put("mensaje", "You are not suscribed!");

		}

		return this.showMeeting(meetingId, modelMap);
	}

}
