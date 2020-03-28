
package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.New;
import org.springframework.transaction.annotation.Transactional;

public interface NewRepository {

	@Transactional(readOnly = true)
	List<Integer> getNewsFromBook(int bookId) throws DataAccessException;

	@Transactional
	@Modifying
	void deleteNew(int newId) throws DataAccessException;

	@Transactional(readOnly = true)
	Boolean existsById(int newId) throws DataAccessException;

	@Transactional
	Collection<New> getAllNews() throws DataAccessException;

	@Transactional
	New findById(int newId) throws DataAccessException;

	New save(New neew) throws DataAccessException;

}
