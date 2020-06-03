
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.List;


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
	ReadBook getReadBookByBookIdAndUsername(int bookId, String username) ;
	
	@Override
	@Transactional
	@Modifying
	@Query("DELETE FROM ReadBook WHERE book.id=?1")
	void deleteByBookId(int bookId) ;
	
	@Override
	@Transactional
	@Query(value="SELECT book_id,COUNT(book_id) FROM read_book GROUP BY book_id ORDER BY COUNT(book_id) DESC Limit 0,10", nativeQuery=true)
	List<Integer> getTopReadBooks();
}
