
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.New;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.samples.petclinic.service.NewService;
import org.springframework.samples.petclinic.service.exceptions.CantDeleteBookInNewException;
import org.springframework.samples.petclinic.service.exceptions.CantShowNewReviewException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NewController {

	private final NewService	newService;
	private final BookService	bookService;


	@Autowired
	public NewController(final NewService newService, final BookService bookService) {
		this.newService = newService;
		this.bookService = bookService;
	}

	@GetMapping(value = "/")
	public String welcome(final Map<String, Object> model) throws CantShowNewReviewException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String authorities = auth.getAuthorities().toString();
		if (authorities.contains("ROLE_ANONYMOUS")) {
			return "redirect:/news";
		} else {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			Collection<New> results2 = this.newService.getNewsBookReview2(userDetail.getUsername());
			if (results2.isEmpty()) {
				return "redirect:/news";
			}
			Collection<New> results = this.newService.getNewsBookReview(userDetail.getUsername());
			model.put("AllNews", true);
			model.put("news", results);
			return "news/newList";
		}

	}

	@GetMapping(value = "/news")
	public String allNews(final Map<String, Object> model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String authorities = auth.getAuthorities().toString();
		
		if (!authorities.contains("ROLE_ANONYMOUS")) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			model.put("NewsRec", true);
			Boolean canShow = this.newService.canShowNewsBookReview(userDetail.getUsername());
				model.put("canShowNewsBookReview", canShow);

		}
		Collection<New> results = this.newService.getAllNews();
		model.put("news", results);
		return "news/newList";
	}
	
	@GetMapping(value = "/news/newsbookreview")
	public String newBookReview(final Map<String, Object> model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userdetails = (UserDetails) auth.getPrincipal();
		Collection<New> results = null;
		try {
			results = this.newService.getNewsBookReview(userdetails.getUsername());
		} catch (CantShowNewReviewException e) {
			return "redirect:/oups";
		}
		model.put("AllNews", true);
		model.put("news", results);
		return "news/newList";
		
		
	}

	@GetMapping("/admin/news/{newId}")
	public String showNew(@PathVariable("newId") final int newId, final Map<String, Object> model) {
		New neew = this.newService.getNewById(newId);
		model.put("new", neew);
		model.put("newId", neew.getId());
		return "news/createOrUpdateNewForm";
	}

	@PostMapping(value = "/admin/news/{newId}")
	public String saveNew(@PathVariable("newId") final int newId, @Valid final New neew, final BindingResult result, final ModelMap modelMap) {
		modelMap.put("newId", neew.getId());
		boolean errorFecha = false;
		LocalDate now = LocalDate.now();
		if (neew.getFecha() != null && now.isBefore(neew.getFecha())) {
			result.rejectValue("fecha", "Publication date cannot be future", "Publication date cannot be future");
			errorFecha = true;
		}
		if (result.hasErrors() || errorFecha) {
			modelMap.addAttribute("new", neew);
			modelMap.addAttribute("hasErrors", true);
			return "news/createOrUpdateNewForm";
		} else {

			New new0 = this.newService.getNewById(newId);
			new0.setHead(neew.getHead());
			new0.setBody(neew.getBody());
			new0.setFecha(neew.getFecha());
			new0.setImg(neew.getImg());
			new0.setName(neew.getName());
			new0.setRedactor(neew.getRedactor());
			new0.setTags(neew.getTags());
			this.newService.save(new0);
			return "redirect:/news";
		}

	}

	@GetMapping("/admin/news/create")
	public String initCreateNew(final Map<String, Object> model) {
		New neew = new New();
		model.put("new", neew);
		model.put("addNew", true);
		return "news/createOrUpdateNewForm";
	}

	@PostMapping(value = "/admin/news/create")
	public String createNew(@Valid final New neew, final BindingResult result, final ModelMap modelMap) {
		boolean errorFecha = false;
		LocalDate now = LocalDate.now();
		if (neew.getFecha() != null && now.isBefore(neew.getFecha())) {
			result.rejectValue("fecha", "Publication date cannot be future", "Publication date cannot be future");
			errorFecha = true;
		}
		if (result.hasErrors() || errorFecha) {
			modelMap.addAttribute("new", neew);
			modelMap.addAttribute("addNew", true);
			return "news/createOrUpdateNewForm";
		} else {
			this.newService.save(neew);
			return "redirect:/admin/news/books/" + neew.getId();
		}

	}

	@GetMapping("/admin/news/delete/{newId}")
	public String deleteNew(@PathVariable("newId") final int newId) {
		this.newService.deleteById(newId);
		return "redirect:/news";
	}

	@GetMapping("/admin/news/books/{newId}")
	public String booksNew(@PathVariable("newId") final int newId, final Map<String, Object> model) {
		Collection<Book> booksIncludes = this.newService.getBooksFromNews(newId);
		model.put("booksIncludes", booksIncludes);

		Collection<Book> booksNotIncludes = this.bookService.findAll();
		booksNotIncludes.removeAll(booksIncludes);
		model.put("booksNotIncludes", booksNotIncludes);
		model.put("newId", newId);
		return "news/bookList";
	}

	@GetMapping("/admin/news/books/save/{newId}")
	public String booksNewSave(@PathVariable("newId") final int newId, final Map<String, Object> model) {
		if (this.newService.getBooksFromNews(newId).isEmpty()) {
			Collection<Book> booksIncludes = this.newService.getBooksFromNews(newId);
			model.put("booksIncludes", booksIncludes);
			Collection<Book> booksNotIncludes = this.bookService.findAll();
			booksNotIncludes.removeAll(booksIncludes);
			model.put("booksNotIncludes", booksNotIncludes);
			model.put("newId", newId);
			model.put("booksNotEmpty", true);
			return "news/bookList";
		}

		return "redirect:/news";
	}

	@GetMapping("/admin/news/books/delete/{newId}/{bookId}")
	public String deleteBooksFromNew(@PathVariable("newId") final int newId, @PathVariable("bookId") final int bookId, final Map<String, Object> model) {
		try {
			this.newService.deleteBookInNew(newId, bookId);
			return "redirect:/admin/news/books/" + newId;
		} catch (CantDeleteBookInNewException e) {
			Collection<Book> booksIncludes = this.newService.getBooksFromNews(newId);
			model.put("booksNotEmpty", true);
			model.put("booksIncludes", booksIncludes);
			Collection<Book> booksNotIncludes = this.bookService.findAll();
			booksNotIncludes.removeAll(booksIncludes);
			model.put("booksNotIncludes", booksNotIncludes);
			model.put("newId", newId);
			return "news/bookList";
		}

	}

	@GetMapping("/admin/news/books/add/{newId}/{bookId}")
	public String addBooksFromNew(@PathVariable("newId") final int newId, @PathVariable("bookId") final int bookId, final Map<String, Object> model) {
		this.newService.saveBookInNew(newId, bookId);
		return "redirect:/admin/news/books/" + newId;
	}

}
