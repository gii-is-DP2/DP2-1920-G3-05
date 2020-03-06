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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class BookController {

	private final BookService bookService;


	@Autowired
	public BookController(final BookService bookService) {
		this.bookService = bookService;
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

}
