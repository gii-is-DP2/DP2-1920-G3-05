
package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.ReadBook;
import org.springframework.transaction.annotation.Transactional;

public interface ReadBookRepository {

	@Transactional(readOnly = true)
	List<Integer> getBooksIdByUsername(String username);

	ReadBook save(ReadBook readBook) throws DataAccessException;
	
	@Transactional
	ReadBook getReadBookByBookIdAndUsername(int bookId, String username) throws DataAccessException;
		
	@Transactional
	@Modifying
	void deleteByBookId(int bookId) throws DataAccessException;

	@Transactional
	Boolean existsById(int readBookId) throws DataAccessException;
	
	@Transactional
	List<Integer> getTopReadBooks() throws DataAccessException;
}
