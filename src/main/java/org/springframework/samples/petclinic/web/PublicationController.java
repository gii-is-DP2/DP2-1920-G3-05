package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Publication;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.samples.petclinic.service.PublicationService;
import org.springframework.samples.petclinic.service.ReadBookService;
import org.springframework.samples.petclinic.service.UserService;
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

public class PublicationController {
	
	private PublicationService publicationService;

	private UserService			userService;

	private BookService 		bookService;

	private ReadBookService		readBookService;
	
	private static final String CONSTANT1= "redirect:/oups";
	
	private static final String CONSTANT2= "publication";
	
	@Autowired
	public PublicationController(final PublicationService publicationService, final UserService userService, final BookService bookService, ReadBookService	readBookService) {
		this.publicationService = publicationService;
		this.userService = userService;
		this.bookService = bookService;
		this.readBookService = readBookService;
	}
	
	@InitBinder("publication")
	public void initPublicationBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new PublicationValidator());
	}
	
	@GetMapping(value = "/books/{bookId}/publications")
	public String listadoDePublications(final ModelMap modelMap,@PathVariable("bookId") final int bookId) {
		Collection<Publication> selections = this.publicationService.findAllPublicationFromBook(bookId);
		modelMap.put("selections", selections);

		return "publications/publicationList";
	}
	@GetMapping("/publications/{publicationId}")
	public ModelAndView showPublication(@PathVariable("publicationId") final int publicationId) {
			Boolean propiedad = false;
			Boolean propiedad2 = false; 
			Publication publication = this.publicationService.findById(publicationId);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetail = (UserDetails) auth.getPrincipal();	
			propiedad = this.publicationService.publicationMioOAdmin(publicationId, userDetail.getUsername());
			propiedad2 = this.publicationService.publicationMio(publicationId, userDetail.getUsername());
			ModelAndView mav = new ModelAndView("publications/publicationDetails");
			mav.addObject(publication);
			mav.addObject("propiedad", propiedad);
			mav.addObject("propiedad2", propiedad2);
			return mav;
		}
	
	
	@GetMapping(value = "/books/{bookId}/publications/publicationAdd")
	public String addPublication(final ModelMap modelMap,@PathVariable("bookId") final int bookId) {
		String view = "publications/publicationAdd";
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		Boolean esLibroLeido = this.readBookService.esReadBook(bookId, userDetail.getUsername());
		if(Boolean.FALSE.equals(esLibroLeido)) {
			return CONSTANT1;
		}
		Publication publication = new Publication();
		Book book = this.bookService.findBookById(bookId);
		publication.setBook(book);
		modelMap.put(CONSTANT2, publication);
		return view;

	}
	
	@PostMapping(value = "/books/{bookId}/publications/save")
	public String savePublication(@Valid final Publication publication, final BindingResult result, final ModelMap modelMap,@PathVariable("bookId") final int bookId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		Boolean esLibroLeido = this.readBookService.esReadBook(bookId, userDetail.getUsername());
		if(Boolean.FALSE.equals(esLibroLeido)) {
			return CONSTANT1;
		}
		User u = this.userService.findUserByUsername(userDetail.getUsername());
		publication.setBook(this.bookService.findBookById(bookId));
		publication.setUser(u);
		publication.setPublicationDate(LocalDate.now());
		

		if (result.hasErrors()) {
			modelMap.addAttribute(CONSTANT2, publication);
			return "publications/publicationAdd";
		}
			this.publicationService.save(publication);
			modelMap.addAttribute("message", "Publication successfully saved!");


		return "redirect:/books/" + bookId  ;
	}
	
	@GetMapping(path = "/publications/{publicationId}/updateForm")
	public String formEvento(final ModelMap modelMap, @PathVariable("publicationId") final int publicationId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		Boolean esMio = this.publicationService.publicationMio(publicationId, userDetail.getUsername());
		if (Boolean.FALSE.equals(esMio)) {
			return CONSTANT1;
		}
		Publication publication = this.publicationService.findById(publicationId);
		modelMap.addAttribute(CONSTANT2, publication);
		return "publications/UpdatePublicationForm";
	}

	@PostMapping("/publications/update/{publicationId}")
	public String updatePublication(@Valid final Publication updatedPublication, final BindingResult result, final ModelMap modelMap, @PathVariable("publicationId") final int publicationId) {
		Publication publication = this.publicationService.findById(publicationId);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		Boolean esMio = this.publicationService.publicationMio(publicationId, userDetail.getUsername());
		if (Boolean.FALSE.equals(esMio)) {
			return CONSTANT1;
		}

		if (result.hasErrors()) {

			modelMap.addAttribute(CONSTANT2, updatedPublication);

			return "publications/UpdatePublicationForm";
		}
		publication.setTitle(updatedPublication.getTitle());
		publication.setDescription(updatedPublication.getDescription());
		publication.setImage(updatedPublication.getImage());
		this.publicationService.save(publication);
		
		modelMap.addAttribute("message", "Publication successfully updated!");

		return "redirect:/publications/" + publicationId;
	}
	
	@GetMapping("/books/{bookId}/delete/{publicationId}")
	public String deleteBook(@PathVariable("publicationId") final int publicationId,@PathVariable("bookId") final int bookId ) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		boolean esMioOAdmin = this.publicationService.publicationMioOAdmin(publicationId, userDetail.getUsername());
		if (!esMioOAdmin) {
			return CONSTANT1;
		}
		this.publicationService.deletePublication(publicationId);
		return "redirect:/books/" + bookId;
	}

}
