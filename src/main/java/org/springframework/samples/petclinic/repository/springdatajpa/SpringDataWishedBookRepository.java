package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.WishedBook;
import org.springframework.samples.petclinic.repository.WishedBookRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SpringDataWishedBookRepository extends WishedBookRepository, CrudRepository<WishedBook, Integer>{

	@Override
	@Transactional
	@Query("SELECT wishedBook.book.id FROM WishedBook wishedBook where wishedBook.user.username=:username")
	List<Integer> getBooksIdByUsername(String username);
	
	@Override
	@Transactional
	@Query("DELETE FROM WishedBook WHERE book.id=?1")
	@Modifying
	void deleteByBookId(int id);
}
