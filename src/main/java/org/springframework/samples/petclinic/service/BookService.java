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
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Genre;
import org.springframework.samples.petclinic.model.Review;
import org.springframework.samples.petclinic.repository.BookRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedISBNException;
import org.springframework.samples.petclinic.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

	private BookRepository bookRepository;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private MeetingService meetingService;

	@Autowired
	private PublicationService publicationService;

	@Autowired
	NewService newService;

	@Autowired
	public BookService(final BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Transactional(readOnly = true)
	public Collection<Genre> findGenre() throws DataAccessException {
		return this.bookRepository.findGenre();
	}
	
	@Transactional(readOnly = true) 
	public Genre findGenreByName(final String name) throws DataAccessException { 
 
		return this.bookRepository.findGenreByName(name); 
	} 

	@Transactional(readOnly = true)
	public Collection<Book> findBookByTitleAuthorGenreISBN(final String title) throws DataAccessException {
		return this.bookRepository.findBookByTitleAuthorGenreISBN(title.toUpperCase());
	}

	@Transactional(readOnly = true)
	public Book findBookById(final int id) throws DataAccessException {
		return this.bookRepository.findById(id);

	}

	@Transactional(rollbackFor = DuplicatedISBNException.class)
	public void save(final Book book) throws DataAccessException, DuplicatedISBNException {
		Book bookWithSameIsbn = this.bookRepository.findByISBN(book.getISBN());
		boolean isDuplicated = bookWithSameIsbn != null && bookWithSameIsbn.getId()!=book.getId(); //Que exista y no sea el de mi libro que lo estoy editando
		if (isDuplicated) {
			throw new DuplicatedISBNException();
		} else {
			this.bookRepository.save(book);
		}
	}
	
	@Transactional(readOnly = true)
	public Boolean existsBookById(int bookId) throws DataAccessException {
		return this.bookRepository.existsById(bookId);
	}

	@Transactional
	@Modifying
	public void deleteById(final int id) throws DataAccessException {
		// Vemos si el libro tiene asociadas reviews que haya que borrar previamente
		List<Integer> reviewsId = this.reviewService.getReviewsIdFromBook(id);
		if (reviewsId != null && !reviewsId.isEmpty()) {
			for (Integer i : reviewsId) {
				System.out.println(i);
				this.reviewService.deleteReviewById(i);
			}
		}

		// Vemos si hay reuniones asociadas que borrar previamente
		List<Integer> meetingsId = this.meetingService.getMeetingsFromBook(id);
		if (meetingsId != null && !meetingsId.isEmpty()) {
			for (Integer i : meetingsId) {
				this.meetingService.deleteMeeting(i);
			}
		}

		// Vemos si hay publicaciones asociadas que borrar previamente
		List<Integer> publicationsId = this.publicationService.getPublicationsFromBook(id);
		if (publicationsId != null && !publicationsId.isEmpty()) {
			for (Integer i : publicationsId) {
				this.publicationService.deletePublication(i);
			}
		}

		// Vemos si hay noticias asociadas y hay que ver si hay que borrarlas
		List<Integer> newsId = this.newService.getNewsFromBook(id);
		if (newsId != null && !newsId.isEmpty()) {
			for (Integer i : newsId) {
				this.newService.deleteNew(i, id);
			}
		}
		
		//Borramos el libro
		this.bookRepository.deleteBookById(id);
	}


	@Transactional(readOnly = true)
	public void verifyBook(int bookId) {
		this.bookRepository.verifyBook(bookId);
		
	}
}
