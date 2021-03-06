
package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Meeting;
import org.springframework.samples.petclinic.model.MeetingAssistant;
import org.springframework.samples.petclinic.model.Quote;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.samples.petclinic.service.MeetingAssistantService;
import org.springframework.samples.petclinic.service.MeetingService;
import org.springframework.samples.petclinic.service.QuoteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.CantInscribeMeetingException;
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

	private BookService				bookService;

    private MeetingAssistantService meetingAssistantService;
    
    private UserService	 userService;
    
    private QuoteService quoteService;
    
    private static final String CONSTANT1= "meeting";
    
    private static final String CONSTANT2= "redirect:/oups";
    
    private static final String CONSTANT3= "mensaje";


    @Autowired
    public MeetingController(MeetingService meetingService, BookService bookService, 
    		MeetingAssistantService meetingAssistantService, UserService userService,QuoteService quoteService){
        this.meetingService = meetingService;
        this.bookService = bookService;
        this.meetingAssistantService= meetingAssistantService;
        this.userService = userService;
        this.quoteService = quoteService;
    }

    @GetMapping(value = "/meetings/find")
	public String initFindForm(final Map<String, Object> model) {
		model.put(CONSTANT1, new Meeting());
		Quote quote = this.quoteService.getRandomQuote();
		model.put("quote",quote);
		return "meetings/findMeetings";
    }

    @GetMapping(value = "/meetings")
	public String processFindForm(Meeting meeting, final BindingResult result, final Map<String, Object> model) {

		// allow parameterless GET request for /meetings to return all records
		if (meeting.getName() == null) {
			meeting.setName(""); // empty string signifies broadest possible search
		}

		// find meetings
		Collection<Meeting> results = this.meetingService.findMeetingsByNamePlaceBookTile(meeting.getName());
		if (results.isEmpty()) {
			// no meetings found
			result.rejectValue("name", "notFound", "not found");
			Quote quote = this.quoteService.getRandomQuote();
			model.put("quote",quote);
			return "meetings/findMeetings";
		} else if (results.size() == 1) {
			// 1 meeting found
			meeting = results.iterator().next();
			return "redirect:/meetings/" + meeting.getId();
		} else {
			// multiple meetings found
			model.put("meetings", results);
			return "meetings/meetingsList";
		}
	}
    
	@InitBinder("meeting")
	public void initMeetingBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new MeetingValidator());
	}

    
	@GetMapping(value = "/meetings/{meetingId}")
	public ModelAndView showMeeting(@PathVariable("meetingId") final int meetingId, final ModelMap modelMap) {
		Meeting meeting = this.meetingService.findMeetingById(meetingId);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userdetails = (UserDetails) auth.getPrincipal();
		Optional<Integer> meetingAssistantId = this.meetingAssistantService.findMeetingAssistantByUsernameAndMeetingId(meetingId, userdetails.getUsername());
		ModelAndView mav = new ModelAndView("meetings/meetingDetails");
		Boolean CanInscribe = this.meetingAssistantService.canInscribe(meetingId, userdetails.getUsername(), meeting.getBook().getId());
		List<MeetingAssistant> assistants=this.meetingAssistantService.getAssistantsOfMeeting(meetingId);
		Integer remainingSeats=meeting.getCapacity()-assistants.size();
        mav.addObject("canInscribe", CanInscribe);
		mav.addObject(CONSTANT1, meeting);
		if (meetingAssistantId.isPresent()) {
			modelMap.put("suscribed", true);

		}
		modelMap.addAttribute("assistants", assistants);
		modelMap.addAttribute("remainingSeats", remainingSeats);
		return mav;
	}

	@GetMapping(value = "/admin/books/{bookId}/meetings/new")
	public String addMeeting(final ModelMap modelMap, @PathVariable("bookId") final int bookId) {
		String view = "meetings/meetingAdd";
		Meeting meeting = new Meeting();
		Book book = this.bookService.findBookById(bookId);
		if (Boolean.TRUE.equals(book.getVerified())) {
			meeting.setBook(book);
			modelMap.addAttribute(CONSTANT1, meeting);
			return view;
		} else {
			return CONSTANT2;
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
				return CONSTANT2;
			}
		}
	}
    
	@GetMapping(value = "/meetings/{meetingId}/inscribe")
	public String inscribe(@PathVariable("meetingId") final int meetingId, final ModelMap modelMap) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		Meeting meeting = this.meetingService.findMeetingById(meetingId);
		MeetingAssistant meetingAssistant = new MeetingAssistant();
		User user = this.userService.findUserByUsername(userDetail.getUsername());
		meetingAssistant.setMeeting(meeting);
		meetingAssistant.setUser(user);
		try {
			this.meetingAssistantService.save(meetingAssistant);
		} catch (CantInscribeMeetingException e) {
			return CONSTANT2;
		}
			return "redirect:/meetings";
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
				modelMap.put(CONSTANT3, "You are successfully unsubscribed");
			} else {
				modelMap.put(CONSTANT3, "The meeting has already been held!");
			}
		} else {
			modelMap.put(CONSTANT3, "You are not suscribed!");

		}

		return this.showMeeting(meetingId, modelMap);
	}

}
