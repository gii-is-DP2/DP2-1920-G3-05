package org.springframework.samples.petclinic.repository;

import java.util.List;


import org.springframework.samples.petclinic.model.WishedBook;
import org.springframework.transaction.annotation.Transactional;

public interface WishedBookRepository {

	@Transactional(readOnly = true)
	List<Integer> getBooksIdByUsername(String username);

	WishedBook save(WishedBook wishedBook) ;
	
	void deleteByBookId(int id) ;
	
	Boolean existsById(int id) ;
}
