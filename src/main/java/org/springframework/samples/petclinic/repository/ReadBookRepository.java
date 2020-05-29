
package org.springframework.samples.petclinic.repository;

import java.util.List;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.ReadBook;
import org.springframework.transaction.annotation.Transactional;

public interface ReadBookRepository {

	@Transactional(readOnly = true)
	List<Integer> getBooksIdByUsername(String username);

	ReadBook save(ReadBook readBook) ;
	
	@Transactional
	ReadBook getReadBookByBookIdAndUsername(int bookId, String username) ;
		
	@Transactional
	@Modifying
	void deleteByBookId(int bookId) ;

	@Transactional
	Boolean existsById(int readBookId) ;
	
	@Transactional
	List<Integer> getTopReadBooks() ;
}
