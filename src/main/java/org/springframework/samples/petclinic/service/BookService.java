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

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Genre;
import org.springframework.samples.petclinic.model.Reader;
import org.springframework.samples.petclinic.repository.BookRepository;
import org.springframework.samples.petclinic.service.exceptions.CantDeleteReviewException;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedISBNException;
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
	private NewService newService;
	
	@Autowired
	private ReadBookService readBookService;
	
	@Autowired
	private WishedBookService	wishedBookService;

	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	private ReaderService readerService;

	@Autowired
	public BookService(final BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Transactional(readOnly = true)
	public Collection<Genre> findGenre()  {
		return this.bookRepository.findGenre();
	}
	
	@Transactional(readOnly = true) 
	public Genre findGenreByName(final String name)  { 
 
		return this.bookRepository.findGenreByName(name); 
	} 

	@Transactional(readOnly = true)
	public Collection<Book> findBookByTitleAuthorGenreISBN(final String title)  {
		return this.bookRepository.findBookByTitleAuthorGenreISBN(title.toUpperCase());
	}

	@Transactional(readOnly = true)
	public Book findBookById(final int id)  {
		return this.bookRepository.findById(id);

	}

	@Transactional(rollbackFor = DuplicatedISBNException.class)
	public void save(final Book book) throws DuplicatedISBNException {
		Boolean imAdmin = false;


		Reader reader = this.readerService.findReaderByUsername(book.getUser().getUsername());
		
		for (Authorities ga : this.authoritiesService.getAuthoritiesByUsername(book.getUser().getUsername())) {
			if (ga.getAuthority().equals("admin")) {
				imAdmin = true;
			}
		}

		book.setVerified(imAdmin || reader.getVerified());

		try{
			String isbn = book.getISBN();
			Book bookWithSameIsbn = this.bookRepository.findByISBN(isbn);
			boolean isDuplicated = bookWithSameIsbn != null && bookWithSameIsbn.getId()!=book.getId(); //Que exista y no sea el de mi libro que lo estoy editando
			if (isDuplicated) {
				throw new DuplicatedISBNException();
			} else {
				this.bookRepository.save(book);
			}
		}catch(Exception e){
			throw new DuplicatedISBNException();
		}
	}
	
	@Transactional(readOnly = true)
	public Boolean existsBookById(int bookId)  {
		return this.bookRepository.existsById(bookId);
	}

	@Transactional
	@Modifying
	public void deleteById(final int id, String username)  {

		deleteReviews(username, this.reviewService.getReviewsIdFromBook(id));
		deleteMeetings(this.meetingService.getMeetingsIdFromBook(id));
		deletePublications(this.publicationService.getPublicationsFromBook(id));
		deleteNews(id, this.newService.getNewsFromBook(id));
		this.readBookService.deleteReadBookByBookId(id);
		this.wishedBookService.deleteByBookId(id);
		this.bookRepository.deleteBookById(id);
	}

	private void deleteNews(final int id, List<Integer> newsId) {
		if (newsId != null && !newsId.isEmpty()) {
			for (Integer i : newsId) {
				this.newService.deleteNew(i, id);
			}
		}
	}

	private void deletePublications(List<Integer> publicationsId) {
		if (publicationsId != null && !publicationsId.isEmpty()) {
			for (Integer i : publicationsId) {
				this.publicationService.deletePublication(i);
			}
		}
	}

	private void deleteMeetings(List<Integer> meetingsId) {
		if (meetingsId != null && !meetingsId.isEmpty()) {
			for (Integer i : meetingsId) {
				this.meetingService.deleteMeeting(i);
			}
		}
	}

	private void deleteReviews(String username, List<Integer> reviewsId) {
		if (reviewsId != null && !reviewsId.isEmpty()) {
			for (Integer i : reviewsId) {
				try {
					this.reviewService.deleteReviewById(i, username);
				}catch (CantDeleteReviewException e) {
					
				}
			}
		}
	}

	@Transactional()
	@Modifying
	public void verifyBook(final int bookId) {
		this.bookRepository.verifyBook(bookId);

	}

	@Transactional(readOnly = true)
	public Collection<Book> findAll() {
		return this.bookRepository.findAll();
	}

	
	public Boolean canEditBook(int bookId, String username) {
		Boolean res = false;
		Boolean imAdmin = false;
		Book book = this.findBookById(bookId);
		List<Authorities> authorities = this.authoritiesService.getAuthoritiesByUsername(username);
		for (Authorities a: authorities) {
			if (a.getAuthority().equals("admin")) {
				imAdmin = true;
			}
		}
		if (username.equals(book.getUser().getUsername()) && !book.getVerified() || Boolean.TRUE.equals(imAdmin)) {
			res = true;
		}

		return res;
	}
	@Transactional(readOnly = true)
	public List<Boolean> getVerifiedFromBooksByUsername(final String username)  {
		return this.bookRepository.getVerifiedFromBooksByUsername(username);

	}

}
