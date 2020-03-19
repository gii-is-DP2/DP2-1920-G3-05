
package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.ReadBook;
import org.springframework.samples.petclinic.repository.ReadBookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReadBookService {

	@Autowired
	private ReadBookRepository readBookRepository;


	@Transactional
	public List<Integer> findBooksIdByUser(final String username) {
		return this.readBookRepository.getBooksIdByUsername(username);
	}

	@Transactional
	public void save(final ReadBook readBook) {
		this.readBookRepository.save(readBook);
	}
	
	@Transactional
	public Boolean esReadBook(final int bookId, final String username) throws DataAccessException{
		ReadBook rb = this.readBookRepository.getReadBookByBookIdAndUsername(bookId, username);
		if(rb==null) {
			return false;
		}else{
			return true;
		}
	}

	
	@Transactional
	@Modifying
	public void deleteReadBookByBookId(int readBookId) throws DataAccessException{
		this.readBookRepository.deleteByBookId(readBookId);
	}

	@Transactional
	public Boolean existsReadBook(int readBookId) throws DataAccessException {
		return this.readBookRepository.existsById(readBookId);
	}

}
