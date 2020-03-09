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

package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Genre;
import org.springframework.samples.petclinic.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

	private BookRepository bookRepository;


	@Autowired
	public BookService(final BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Transactional(readOnly = true)
	public Collection<Genre> findGenre() throws DataAccessException {
		return this.bookRepository.findGenre();
	}

	@Transactional(readOnly = true)
	public Collection<Book> findBookByTitleAuthorGenreISBN(final String title) throws DataAccessException {
		return this.bookRepository.findBookByTitleAuthorGenreISBN(title.toUpperCase());
	}

	@Transactional(readOnly = true)
	public Book findBookById(final int id) throws DataAccessException {
		return this.bookRepository.findById(id);
	}

	@Transactional
	public void save(final Book book) {
		this.bookRepository.save(book);
	}
}
