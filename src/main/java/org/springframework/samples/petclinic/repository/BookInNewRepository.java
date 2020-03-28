
package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.BookInNew;
import org.springframework.transaction.annotation.Transactional;

public interface BookInNewRepository {

	@Transactional(readOnly = true)
	List<Integer> getBooksInNewFromNew(int newId) throws DataAccessException;

	@Transactional
	@Modifying
	void deleteBookInNew(int newId, int bookId) throws DataAccessException;

	@Transactional(readOnly = true)
	Collection<Book> getBooksInNew(int newId) throws DataAccessException;

	@Transactional
	@Modifying
	BookInNew save(BookInNew bn) throws DataAccessException;

	@Transactional(readOnly = true)
	BookInNew getByNewIdBookId(int newId, int bookId) throws DataAccessException;

}
