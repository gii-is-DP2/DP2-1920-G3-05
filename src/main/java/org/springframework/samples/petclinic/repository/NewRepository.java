
package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.New;
import org.springframework.transaction.annotation.Transactional;

public interface NewRepository {

	@Transactional(readOnly = true)
	List<Integer> getNewsFromBook(int bookId) ;

	@Transactional
	@Modifying
	void deleteNew(int newId) ;

	@Transactional(readOnly = true)
	Boolean existsById(int newId) ;

	@Transactional
	Collection<New> getAllNews() ;

	@Transactional
	New findById(int newId) ;

	New save(New neew) ;

	@Transactional
	Collection<New> getNewsBookReview(String userId) ;

}
