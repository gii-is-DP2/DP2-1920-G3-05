
package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.ReadBook;
import org.springframework.transaction.annotation.Transactional;

public interface ReadBookRepository {

	@Transactional(readOnly = true)
	List<Integer> getBooksIdByUsername(String username);

	void save(ReadBook readBook) throws DataAccessException;
}
