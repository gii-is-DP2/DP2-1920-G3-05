
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.BookInNew;
import org.springframework.samples.petclinic.repository.BookInNewRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SpringDataBookInNewRepository extends BookInNewRepository, CrudRepository<BookInNew, Integer> {

	@Override
	@Transactional
	@Query("SELECT bookInNew.book.id FROM BookInNew bookInNew WHERE bookInNew.neew.id = ?1")
	List<Integer> getBooksInNewFromNew(int newId);

	@Override
	@Transactional
	@Modifying
	@Query("DELETE FROM BookInNew WHERE neew.id = ?1 AND book.id = ?2")
	void deleteBookInNew(int newId, int bookId);

	@Override
	@Transactional
	@Query("SELECT bookInNew.book FROM BookInNew bookInNew WHERE bookInNew.neew.id = ?1")
	Collection<Book> getBooksInNew(int newId);

}
