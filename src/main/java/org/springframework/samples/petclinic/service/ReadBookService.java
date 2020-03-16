
package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

}
