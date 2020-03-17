package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.WishedBook;
import org.springframework.samples.petclinic.repository.ReadBookRepository;
import org.springframework.samples.petclinic.repository.WishedBookRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedISBNException;
import org.springframework.samples.petclinic.service.exceptions.ReadOrWishedBookException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishedBookService {

	@Autowired
	private WishedBookRepository wishedBookRepository;
	
	@Autowired
	private ReadBookRepository readBookRepository;


	@Transactional
	public List<Integer> findBooksIdByUser(final String username) {
		return this.wishedBookRepository.getBooksIdByUsername(username);
	}

	@Transactional
	public void save(final WishedBook wishedBook) throws DataAccessException,ReadOrWishedBookException {
		Boolean isReadOrWished=this.findBooksIdByUser(wishedBook.getUser().getUsername()).contains(wishedBook.getBook().getId())||readBookRepository.getBooksIdByUsername(wishedBook.getUser().getUsername()).contains(wishedBook.getBook().getId());
		if (isReadOrWished) {
			throw new ReadOrWishedBookException("This book is already read or in the wish list.");
		} else {
			this.wishedBookRepository.save(wishedBook);
		}
		

	}
	
	@Transactional
	public void deleteByBookId(final int id) {
		this.wishedBookRepository.deleteByBookId(id);

	}
}
