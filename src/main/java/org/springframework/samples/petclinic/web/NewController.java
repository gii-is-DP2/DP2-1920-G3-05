/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

	private static final String constant1= "redirect:/news";
	
	private static final String constant2= "news/newList";
	
	private static final String constant3= "newId";
	
	private static final String constant4= "news/createOrUpdateNewForm";
	
	private static final String constant5= "Publication date cannot be future";
	
	private static final String constant6= "redirect:/admin/news/books/";
	
	private static final String constant7= "booksIncludes";
	
	private static final String constant8= "booksNotIncludes";
	
	private static final String constant9= "news/bookList";

	@Autowired
	public NewController(final NewService newService, final BookService bookService) {
		this.newService = newService;
		this.bookService = bookService;
	}

	@GetMapping(value = "/")
	public String welcome(final Map<String, Object> model) throws DataAccessException, CantShowNewReviewException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String authorities = auth.getAuthorities().toString();
		if (authorities.contains("ROLE_ANONYMOUS")) {
			return constant1;
		} else {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			Collection<New> results2 = this.newService.getNewsBookReview2(userDetail.getUsername());
			//Collection<New> results = this.newService.getAllNews();
			if (results2.isEmpty()) {
				return constant1;
			}
			Collection<New> results = this.newService.getNewsBookReview(userDetail.getUsername());
			model.put("AllNews", true);
			model.put("news", results);
			return constant2;
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
			if(Boolean.FALSE.equals(canShow)) {
				model.put("canShowNewsBookReview", false);
			}else {
				model.put("canShowNewsBookReview", true);
			}

		}
		Collection<New> results = this.newService.getAllNews();
		model.put("news", results);
		return constant2;
	}
	
	@GetMapping(value = "/news/newsbookreview")
	public String newBookReview(final Map<String, Object> model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userdetails = (UserDetails) auth.getPrincipal();
		Collection<New> results = null;
		try {
			results = this.newService.getNewsBookReview(userdetails.getUsername());
		} catch (CantShowNewReviewException e) {
			// TODO Auto-generated catch block
			return "redirect:/oups";
		}
		model.put("AllNews", true);
		model.put("news", results);
		return constant2;
		
		
	}

	@GetMapping("/admin/news/{newId}")
	public String showNew(@PathVariable("newId") final int newId, final Map<String, Object> model) {
		New neew = this.newService.getNewById(newId);
		model.put("new", neew);
		model.put(constant3, neew.getId());
		return constant4;
	}

	@PostMapping(value = "/admin/news/{newId}")
	public String saveNew(@PathVariable("newId") final int newId, @Valid final New neew, final BindingResult result, final ModelMap modelMap) {
		modelMap.put(constant3, neew.getId());
		boolean errorFecha = false;
		LocalDate now = LocalDate.now();
		if (neew.getFecha() != null && now.isBefore(neew.getFecha())) {
			result.rejectValue("fecha", constant5, constant5);
			errorFecha = true;
		}
		if (result.hasErrors() || errorFecha) {
			modelMap.addAttribute("new", neew);
			modelMap.addAttribute("hasErrors", true);
			return constant4;
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
			return constant1;
		}

	}

	@GetMapping("/admin/news/create")
	public String initCreateNew(final Map<String, Object> model) {
		New neew = new New();
		model.put("new", neew);
		model.put("addNew", true);
		return constant4;
	}

	@PostMapping(value = "/admin/news/create")
	public String createNew(@Valid final New neew, final BindingResult result, final ModelMap modelMap) {
		boolean errorFecha = false;
		LocalDate now = LocalDate.now();
		if (neew.getFecha() != null && now.isBefore(neew.getFecha())) {
			result.rejectValue("fecha", constant5, constant5);
			errorFecha = true;
		}
		if (result.hasErrors() || errorFecha) {
			modelMap.addAttribute("new", neew);
			modelMap.addAttribute("addNew", true);
			return constant4;
		} else {
			this.newService.save(neew);
			return constant6 + neew.getId();
		}

	}

	@GetMapping("/admin/news/delete/{newId}")
	public String deleteNew(@PathVariable("newId") final int newId) {
		this.newService.deleteById(newId);
		return constant1;
	}

	@GetMapping("/admin/news/books/{newId}")
	public String booksNew(@PathVariable("newId") final int newId, final Map<String, Object> model) {
		Collection<Book> booksIncludes = this.newService.getBooksFromNews(newId);
		model.put(constant7, booksIncludes);

		Collection<Book> booksNotIncludes = this.bookService.findAll();
		booksNotIncludes.removeAll(booksIncludes);
		model.put(constant8, booksNotIncludes);
		model.put(constant3, newId);
		return constant9;
	}

	@GetMapping("/admin/news/books/save/{newId}")
	public String booksNewSave(@PathVariable("newId") final int newId, final Map<String, Object> model) {
		if (this.newService.getBooksFromNews(newId).isEmpty()) {
			Collection<Book> booksIncludes = this.newService.getBooksFromNews(newId);
			model.put(constant7, booksIncludes);
			Collection<Book> booksNotIncludes = this.bookService.findAll();
			booksNotIncludes.removeAll(booksIncludes);
			model.put(constant8, booksNotIncludes);
			model.put(constant3, newId);
			model.put("booksNotEmpty", true);
			return constant9;
		}

		return constant1;
	}

	@GetMapping("/admin/news/books/delete/{newId}/{bookId}")
	public String deleteBooksFromNew(@PathVariable("newId") final int newId, @PathVariable("bookId") final int bookId, final Map<String, Object> model) {
		try {
			this.newService.deleteBookInNew(newId, bookId);
			return constant6 + newId;
		} catch (CantDeleteBookInNewException e) {
			Collection<Book> booksIncludes = this.newService.getBooksFromNews(newId);
			model.put("booksNotEmpty", true);
			model.put(constant7, booksIncludes);
			Collection<Book> booksNotIncludes = this.bookService.findAll();
			booksNotIncludes.removeAll(booksIncludes);
			model.put(constant8, booksNotIncludes);
			model.put(constant3, newId);
			return constant9;
		}

	}

	@GetMapping("/admin/news/books/add/{newId}/{bookId}")
	public String addBooksFromNew(@PathVariable("newId") final int newId, @PathVariable("bookId") final int bookId, final Map<String, Object> model) {
		this.newService.saveBookInNew(newId, bookId);
		return constant6 + newId;
	}

}
