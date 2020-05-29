package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Publication;
import org.springframework.transaction.annotation.Transactional;

public interface PublicationRepository{

	@Transactional(readOnly = true)
	List<Integer> getPublicationsFromBook(int bookId) ;
	
	@Transactional
	@Modifying
	void deletePublication(int publicationId) ;
	
	@Transactional(readOnly = true)
	Boolean existsById(int publicationId) ;
	
	@Transactional(readOnly = true)
	Collection<Publication> getAllPublicationsFromBook(int bookId) ;
	
	@Transactional(readOnly=true)
	Publication findById(int id) ;
	
	
	Publication save(Publication publication) ;
}
