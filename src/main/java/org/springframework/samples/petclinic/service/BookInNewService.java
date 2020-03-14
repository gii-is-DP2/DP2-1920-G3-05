package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.repository.BookInNewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookInNewService {

	@Autowired
	private BookInNewRepository bookInNewRepository;
	
	@Transactional(readOnly = true)
	public List<Integer> getBooksInNewFromNew(int newId) throws DataAccessException {
		return this.bookInNewRepository.getBooksInNewFromNew(newId);
	}

	
	@Transactional
	@Modifying
	public void deleteBookInNew(int newId, int bookId) throws DataAccessException {
		this.bookInNewRepository.deleteBookInNew(newId, bookId);
	}
	
}
