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

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Genre;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedISBNException;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class BookController {

	private final BookService	bookService;

	@Autowired
	private UserService			userService;


	@Autowired
	public BookController(final BookService bookService) {
		this.bookService = bookService;
	}

	@ModelAttribute("genres")
	public Collection<Genre> populateGenre() {
		return this.bookService.findGenre();
	}

	@InitBinder("book")
	public void initBookBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new BookValidator());
	}

	@GetMapping(value = "/books/find")
	public String initFindForm(final Map<String, Object> model) {
		model.put("book", new Book());
		return "books/findBooks";
	}

	@GetMapping(value = "/books")
	public String processFindForm(Book book, final BindingResult result, final Map<String, Object> model) {

		// allow parameterless GET request for /books to return all records
		if (book.getTitle() == null) {
			book.setTitle(""); // empty string signifies broadest possible search
		}

		// find books by title
		Collection<Book> results = this.bookService.findBookByTitleAuthorGenreISBN(book.getTitle());
		if (results.isEmpty()) {
			// no books found
			result.rejectValue("title", "notFound", "not found");
			return "books/findBooks";
		} else if (results.size() == 1) {
			// 1 book found
			book = results.iterator().next();
			return "redirect:/books/" + book.getId();
		} else {
			// multiple books found
			model.put("selections", results);
			return "books/booksList";
		}
	}

	@GetMapping("/books/{bookId}")
	public ModelAndView showBook(@PathVariable("bookId") final int bookId) {
		ModelAndView mav = new ModelAndView("books/bookDetails");
		mav.addObject(this.bookService.findBookById(bookId));
		return mav;
	}

	@GetMapping(value = "/books/add")
	public String addBook(final ModelMap modelMap) {
		String view = "books/bookAdd";
		modelMap.addAttribute("book", new Book());
		return view;
	}

	@PostMapping(value = "/books/save")
	public String saveBook(@Valid final Book book, final BindingResult result, final ModelMap modelMap) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		User u = new User();
		u = this.userService.findUserByUsername(userDetail.getUsername());
		book.setUser(u);
		Boolean imAdmin = false;
		for (GrantedAuthority ga : userDetail.getAuthorities()) {
			if (ga.getAuthority().equals("admin")) {
				imAdmin = true;
			}
		}

		if (imAdmin) {
			book.setVerified(true);
		} else {
			book.setVerified(false);
		}

		if (result.hasErrors()) {
			modelMap.addAttribute("book", book);
			return "books/bookAdd";
		} else {
			try {
				this.bookService.save(book);
			} catch (DuplicatedISBNException ex) {
				result.rejectValue("ISBN", "duplicate", "already exists");
				return "books/bookAdd";
			}
			modelMap.addAttribute("message", "Book successfully saved!");
		}

		return "redirect:/books";
	}

}
