
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Genre;

public interface BookRepository {

	Book findById(int id) throws DataAccessException;

	Collection<Book> findBookByTitleAuthorGenreISBN(String title) throws DataAccessException;

	void save(Book book) throws DataAccessException;

	Collection<Genre> findBookGenres() throws DataAccessException;

	Genre findGenreByName(String name) throws DataAccessException;
}
