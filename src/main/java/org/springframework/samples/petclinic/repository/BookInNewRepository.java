package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface BookInNewRepository {

	@Transactional(readOnly = true)
	List<Integer>  getBooksInNewFromNew(int newId) throws DataAccessException;

	@Transactional
	@Modifying
	void deleteBookInNew(int newId, int bookId) throws DataAccessException;
	
}