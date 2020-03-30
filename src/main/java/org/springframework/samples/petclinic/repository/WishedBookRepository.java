package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.WishedBook;
import org.springframework.transaction.annotation.Transactional;

public interface WishedBookRepository {

	@Transactional(readOnly = true)
	List<Integer> getBooksIdByUsername(String username);

	WishedBook save(WishedBook wishedBook) throws DataAccessException;
	
	void deleteByBookId(int id) throws DataAccessException;
	
	Boolean existsById(int id) throws DataAccessException;
}
