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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Genre;
import org.springframework.samples.petclinic.model.ReadBook;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.WishedBook;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.samples.petclinic.service.ReadBookService;
import org.springframework.samples.petclinic.service.ReviewService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.WishedBookService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedISBNException;
import org.springframework.samples.petclinic.service.exceptions.ReadOrWishedBookException;
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

	private UserService			userService;

	private ReadBookService		readBookService;

	private ReviewService		reviewService;

	private WishedBookService	wishedBookService;


	@Autowired
	public BookController(final BookService bookService, final UserService userService, final ReadBookService readBookService, final WishedBookService wishedBookService, final ReviewService reviewService) {
		this.bookService = bookService;
		this.userService = userService;
		this.readBookService = readBookService;
		this.wishedBookService = wishedBookService;
		this.reviewService = reviewService;

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

	@ModelAttribute("genres")
	public Collection<Genre> populatePetTypes() {
		return this.bookService.findGenre();
	}
	@ModelAttribute("recomendations")
	public Collection<Book> populateRecomendations() {
		List<Book> selections = new ArrayList<>();
		List<String> genres = new ArrayList<>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userdetails = (UserDetails) auth.getPrincipal();
		List<Integer> ids = this.readBookService.findBooksIdByUser(userdetails.getUsername());

		if (ids.isEmpty()) {
			return this.bookService.findAll();
		}

		for (Integer i : ids) {
			Book book = this.bookService.findBookById(i);
			selections.add(book);
			genres.add(book.getGenre().getName());
		}

		String genreName = BookController.maxGenre(genres);
		List<Book> recomendations = (List<Book>) this.bookService.findBookByTitleAuthorGenreISBN(genreName);
		recomendations.removeAll(selections);
		return recomendations;
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
	@GetMapping("/books/readBooks")
	public String listadoDeLibrosLeidos(final ModelMap modelMap) {
		List<Book> selections = new ArrayList<>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userdetails = (UserDetails) auth.getPrincipal();

		List<Integer> ids = this.readBookService.findBooksIdByUser(userdetails.getUsername());
		for (Integer i : ids) {
			selections.add(this.bookService.findBookById(i));

		}
		modelMap.put("selections", selections);

		return "books/booksList";
	}
	@PostMapping("/books/readBooks/{bookId}")
	public String anadirLibrolistadoDeLibrosLeidos(@PathVariable("bookId") final int bookId, final ModelMap modelMap) {

		Book book = this.bookService.findBookById(bookId);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userdetails = (UserDetails) auth.getPrincipal();
		User user = this.userService.findUserByUsername(userdetails.getUsername());
		ReadBook readBook = new ReadBook();
		readBook.setBook(book);
		readBook.setUser(user);
		this.readBookService.save(readBook);
		this.wishedBookService.deleteByBookId(bookId);

		return "redirect:/books";
	}
	@GetMapping("/books/{bookId}")
	public ModelAndView showBook(@PathVariable("bookId") final int bookId) {
		Boolean propiedad = false;
		Boolean noEsReadBook = false;
		Boolean notWishedBook = false;
		Boolean hasAnyReview = false;
		Boolean canWriteReview;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		if (!this.reviewService.getReviewsFromBook(bookId).isEmpty()) {
			hasAnyReview = true;
		}
		Book book = this.bookService.findBookById(bookId);
		noEsReadBook = !this.readBookService.esReadBook(bookId, userDetails.getUsername());
		notWishedBook = !this.wishedBookService.esWishedBook(bookId);
		canWriteReview = this.reviewService.canWriteReview(bookId, userDetails.getUsername());
		propiedad = this.bookService.canEditBook(bookId, userDetails.getUsername());

		ModelAndView mav = new ModelAndView("books/bookDetails");
		mav.addObject(book);
		mav.addObject("propiedad", propiedad);
		mav.addObject("noEsReadBook", noEsReadBook);
		mav.addObject("notWishedBook", notWishedBook);
		mav.addObject("hasAnyReview", hasAnyReview);
		mav.addObject("canWriteReview", canWriteReview);
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

	@GetMapping(path = "/books/{bookId}/updateForm")
	public String formUpdateBooks(final ModelMap modelMap, @PathVariable("bookId") final int bookId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userdetails = (UserDetails) auth.getPrincipal();
		if (!this.bookService.canEditBook(bookId, userdetails.getUsername())) {
			return "redirect:/oups";
		}
		modelMap.addAttribute("book", this.bookService.findBookById(bookId));
		return "books/UpdateBookForm";
	}

	@PostMapping("/books/update/{bookId}")
	public String updateBooks(@Valid final Book updatedBook, final BindingResult result, final ModelMap modelMap, @PathVariable("bookId") final int bookId) {

		Book book = this.bookService.findBookById(bookId);

		Boolean verified = book.getVerified();

		if (result.hasErrors()) {

			modelMap.addAttribute("book", updatedBook);

			return "/books/UpdateBookForm";
		}
		Genre genre = this.bookService.findGenreByName(updatedBook.getGenre().getName());
		book.setGenre(genre);
		book.setAuthor(updatedBook.getAuthor());
		book.setEditorial(updatedBook.getEditorial());
		book.setImage(updatedBook.getImage());
		book.setISBN(updatedBook.getISBN());
		book.setPages(updatedBook.getPages());
		book.setPublicationDate(updatedBook.getPublicationDate());
		book.setSynopsis(updatedBook.getSynopsis());
		book.setTitle(updatedBook.getTitle());
		if (verified.equals(true)) {
			book.setVerified(true);
		} else {
			book.setVerified(false);
		}

		try {
			this.bookService.save(book);
		} catch (DuplicatedISBNException ex) {
			result.rejectValue("ISBN", "duplicate", "already exists");
			return "books/UpdateBookForm";
		}

		modelMap.addAttribute("message", "Book successfully updated!");

		return "redirect:/books/{bookId}";
	}

	@GetMapping("/admin/books/delete/{bookId}")
	public String deleteBook(@PathVariable("bookId") final int bookId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userdetails = (UserDetails) auth.getPrincipal();
		this.bookService.deleteById(bookId, userdetails.getUsername());
		return "redirect:/books";
	}

	@GetMapping("admin/books/{bookId}/verify")
	public String verifyBook(@PathVariable("bookId") final int bookId) {
		this.bookService.verifyBook(bookId);
		return "redirect:/books/" + bookId;
	}

	/*
	 * private Boolean esReadBook(final Integer id) {
	 * Boolean res = false;
	 * Book book = this.bookService.findBookById(id);
	 * Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	 * UserDetails userdetails = (UserDetails) auth.getPrincipal();
	 * List<Integer> ids = this.readBookService.findBooksIdByUser(userdetails.getUsername());
	 * for (Integer i : ids) {
	 * if (this.bookService.findBookById(i).getId().equals(book.getId())) {
	 * res = true;
	 * }
	 * }
	 * return res;
	 * }
	 */

	@GetMapping("/books/wishList")
	public String listadoDeLibrosDeseados(final ModelMap modelMap) {
		List<Book> selections = new ArrayList<>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userdetails = (UserDetails) auth.getPrincipal();

		List<Integer> ids = this.wishedBookService.findBooksIdByUser(userdetails.getUsername());
		for (Integer i : ids) {
			selections.add(this.bookService.findBookById(i));

		}
		modelMap.put("selections", selections);

		return "books/booksList";
	}
	@PostMapping("/books/wishList/{bookId}")
	public String anadirLibroListaDeseados(@PathVariable("bookId") final int bookId, final ModelMap modelMap) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userdetails = (UserDetails) auth.getPrincipal();
		if (this.wishedBookService.esWishedBook(bookId) || this.readBookService.esReadBook(bookId, userdetails.getUsername())) {
			return "redirect:/oups";
		}
		Book book = this.bookService.findBookById(bookId);

		User user = this.userService.findUserByUsername(userdetails.getUsername());
		WishedBook wishedBook = new WishedBook();
		wishedBook.setBook(book);
		wishedBook.setUser(user);
		try {
			this.wishedBookService.save(wishedBook);
		} catch (ReadOrWishedBookException e) {
			return "redirect:/oups";
		}

		return "redirect:/books";
	}
	public static String maxGenre(final List<String> genres) {
		String genre = genres.get(0);
		Integer max = 0;

		for (String g : genres) {
			Integer count = 0;
			for (int i = 0; i < genres.size(); i++) {
				if (g.equals(genres.get(i))) {
					count++;
				}
			}
			if (count > max) {
				max = count;
				genre = g;
			}
		}
		return genre;
	}
}
