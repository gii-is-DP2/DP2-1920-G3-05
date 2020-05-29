
package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;


import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Genre;

public interface BookRepository {

	Book findById(int id) ;

	Collection<Book> findBookByTitleAuthorGenreISBN(String title) ;

	List<Genre> findGenre() ;

	Book save(Book book) ;

	Book findByISBN(String ISBN) ;

	Genre findGenreByName(String name) ;

	void deleteBookById(int bookId) ;

	Boolean existsById(int bookId) ;

	Collection<Book> findAll() ;

	void verifyBook(int bookId);

	List<Boolean> getVerifiedFromBooksByUsername(String username);
}
