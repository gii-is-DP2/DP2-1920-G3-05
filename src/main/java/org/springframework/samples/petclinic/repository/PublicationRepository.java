package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Publication;
import org.springframework.transaction.annotation.Transactional;

public interface PublicationRepository{

	@Transactional(readOnly = true)
	List<Integer> getPublicationsFromBook(int bookId) throws DataAccessException;
	
	@Transactional
	@Modifying
	void deletePublication(int publicationId) throws DataAccessException;
	
	@Transactional(readOnly = true)
	Boolean existsById(int publicationId) throws DataAccessException;
	
	@Transactional(readOnly = true)
	Collection<Publication> getAllPublicationsFromBook(int bookId) throws DataAccessException;
	
	@Transactional(readOnly=true)
	Publication findById(int id) throws DataAccessException;
	
	
	Publication save(Publication publication) throws DataAccessException;
}
