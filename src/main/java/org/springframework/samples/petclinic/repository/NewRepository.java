
package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface NewRepository {
	
	@Transactional(readOnly = true)
	List<Integer> getNewsFromBook(int bookId) throws DataAccessException;
	
	@Transactional
	@Modifying
	void deleteNew(int newId) throws DataAccessException;
	
	@Transactional(readOnly = true)
	Boolean existsById(int newId) throws DataAccessException;
	
}
