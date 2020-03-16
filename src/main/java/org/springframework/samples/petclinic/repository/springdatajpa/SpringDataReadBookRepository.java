
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.ReadBook;
import org.springframework.samples.petclinic.repository.ReadBookRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SpringDataReadBookRepository extends ReadBookRepository, CrudRepository<ReadBook, Integer> {

	@Override
	@Transactional
	@Query("SELECT readBook.book.id FROM ReadBook readBook where readBook.user.username=:username")
	List<Integer> getBooksIdByUsername(String username);
	
	@Override
	@Transactional
	@Query("SELECT rb FROM ReadBook rb where rb.book.id = ?1 AND rb.user.username = ?2")
	ReadBook getReadBookByBookIdAndUsername(int bookId, String username) throws DataAccessException;

}
