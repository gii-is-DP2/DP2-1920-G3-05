package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.WishedBook;
import org.springframework.samples.petclinic.repository.WishedBookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishedBookService {

	@Autowired
	private WishedBookRepository wishedBookRepository;


	@Transactional
	public List<Integer> findBooksIdByUser(final String username) {
		return this.wishedBookRepository.getBooksIdByUsername(username);
	}

	@Transactional
	public void save(final WishedBook wishedBook) {
		this.wishedBookRepository.save(wishedBook);

	}
	
	@Transactional
	public void deleteByBookId(final int id) {
		this.wishedBookRepository.deleteByBookId(id);

	}
	
	@Transactional
	@Modifying
	public void deleteWishedBook(int id) throws DataAccessException{
		this.wishedBookRepository.deleteByBookId(id);
	}
}
