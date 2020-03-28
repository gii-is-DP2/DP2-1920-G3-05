package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Publication;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.samples.petclinic.service.PublicationService;
import org.springframework.samples.petclinic.service.ReadBookService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
	
	@Autowired
	private PublicationService publicationService;

	private UserService			userService;

	@Autowired
	private BookService 		bookService;

	@Autowired
	private ReadBookService		readBookService;
	
	@Autowired
	private AuthoritiesService	authoritiesService;
	
	@Autowired
	public PublicationController(final PublicationService publicationService, final UserService userService) {
		this.publicationService = publicationService;
		this.userService = userService;
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
		if(!esLibroLeido) {
			return "redirect:/oups";
		}
		Publication publication = new Publication();
		Book book = this.bookService.findBookById(bookId);
		publication.setBook(book);
		modelMap.put("publication", publication);
		return view;

	}
	
	@PostMapping(value = "/books/{bookId}/publications/save")
	public String savePublication(@Valid final Publication publication, final BindingResult result, final ModelMap modelMap,@PathVariable("bookId") final int bookId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		Boolean esLibroLeido = this.readBookService.esReadBook(bookId, userDetail.getUsername());
		if(!esLibroLeido) {
			return "redirect:/oups";
		}
		User u = new User();
		u = this.userService.findUserByUsername(userDetail.getUsername());
		publication.setBook(this.bookService.findBookById(bookId));
		publication.setUser(u);
		publication.setPublicationDate(LocalDate.now());
		

		if (result.hasErrors()) {
			modelMap.addAttribute("publication", publication);
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
		if (!esMio) {
			return "redirect:/oups";
		}
		Publication publication = this.publicationService.findById(publicationId);
		modelMap.addAttribute("publication", publication);
		return "publications/UpdatePublicationForm";
	}

	@PostMapping("/publications/update/{publicationId}")
	public String updatePublication(@Valid final Publication updatedPublication, final BindingResult result, final ModelMap modelMap, @PathVariable("publicationId") final int publicationId) {
		Publication publication = this.publicationService.findById(publicationId);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		Boolean esMio = this.publicationService.publicationMio(publicationId, userDetail.getUsername());
		if (!esMio) {
			return "redirect:/oups";
		}

		if (result.hasErrors()) {

			modelMap.addAttribute("publication", updatedPublication);

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
		Boolean esMioOAdmin = this.publicationService.publicationMioOAdmin(publicationId, userDetail.getUsername()); ;
		if (!esMioOAdmin) {
			return "redirect:/oups";
		}
		this.publicationService.deletePublication(publicationId);
		return "redirect:/books/" + bookId;
	}

}
