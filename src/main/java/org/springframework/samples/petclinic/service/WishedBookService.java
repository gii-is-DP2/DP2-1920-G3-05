package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.WishedBook;
import org.springframework.samples.petclinic.repository.BookRepository;
import org.springframework.samples.petclinic.repository.ReadBookRepository;
import org.springframework.samples.petclinic.repository.WishedBookRepository;
import org.springframework.samples.petclinic.service.exceptions.ReadOrWishedBookException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishedBookService {

	@Autowired
	private WishedBookRepository wishedBookRepository;
	
	@Autowired
	private ReadBookRepository readBookRepository;
	
	@Autowired
	private BookRepository bookRepository;


	@Transactional
	public List<Integer> findBooksIdByUser(final String username) {
		return this.wishedBookRepository.getBooksIdByUsername(username);
	}

	@Transactional
	public void save(final WishedBook wishedBook) throws ReadOrWishedBookException {
		Boolean isReadOrWished=this.findBooksIdByUser(wishedBook.getUser().getUsername()).contains(wishedBook.getBook().getId()) || readBookRepository.getBooksIdByUsername(wishedBook.getUser().getUsername()).contains(wishedBook.getBook().getId());
		if (Boolean.TRUE.equals(isReadOrWished)) {
			throw new ReadOrWishedBookException("This book is already read or in the wish list.");
		} else {
			this.wishedBookRepository.save(wishedBook);
		}
		

	}
	
	@Transactional
	public void deleteByBookId(final int id) {
		this.wishedBookRepository.deleteByBookId(id);

	}
	
	@Transactional
	public Boolean esWishedBook(final Integer id) {
		Boolean res = false;
		Book book = this.bookRepository.findById(id);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userdetails = (UserDetails) auth.getPrincipal();
		List<Integer> ids = this.findBooksIdByUser(userdetails.getUsername());
		for (Integer i : ids) {
			if (this.bookRepository.findById(i).getId().equals(book.getId())) {
				res = true;
			}
		}
		return res;
	}
}
