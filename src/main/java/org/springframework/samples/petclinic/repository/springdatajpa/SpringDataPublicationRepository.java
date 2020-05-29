package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Publication;
import org.springframework.samples.petclinic.repository.PublicationRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SpringDataPublicationRepository extends PublicationRepository, CrudRepository<Publication, Integer> {

	@Override
	@Transactional
	@Query("SELECT publication.id FROM Publication publication WHERE publication.book.id = ?1")
	public List<Integer> getPublicationsFromBook(int bookId);
	
	@Override
	@Transactional
	@Modifying
	@Query("DELETE FROM Publication WHERE id = ?1")
	public void deletePublication(int publicationId);
	
	
	@Override
	@Query("SELECT publication FROM Publication publication WHERE publication.book.id = ?1")
	Collection<Publication> getAllPublicationsFromBook(int id);
}
