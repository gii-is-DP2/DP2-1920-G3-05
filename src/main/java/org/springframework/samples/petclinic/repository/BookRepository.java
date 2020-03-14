
package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Genre;

public interface BookRepository {

	Book findById(int id) throws DataAccessException;

	Collection<Book> findBookByTitleAuthorGenreISBN(String title) throws DataAccessException;

	List<Genre> findGenre() throws DataAccessException;

	void save(Book book) throws DataAccessException;

	Book findByISBN(String ISBN) throws DataAccessException;

}
