
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.New;
import org.springframework.samples.petclinic.repository.NewRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SpringDataNewRepository extends NewRepository, CrudRepository<New, Integer> {

	@Override
	@Transactional
	@Query("SELECT DISTINCT bookInNew.neew.id FROM BookInNew bookInNew WHERE bookInNew.book.id = ?1")
	List<Integer> getNewsFromBook(int bookId);

	@Override
	@Transactional
	@Modifying
	@Query("DELETE FROM New WHERE id = ?1")
	void deleteNew(int newId);

	@Override
	@Query("SELECT n FROM New n ORDER BY n.fecha DESC")
	Collection<New> getAllNews();

}
