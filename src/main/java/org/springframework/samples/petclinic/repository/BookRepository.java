
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Book;

public interface BookRepository {

	Book findById(int id) throws DataAccessException;

	Collection<Book> findBookByTitleAuthorGenreISBN(String title) throws DataAccessException;
}
