package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Publication;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.PublicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PublicationService {

	@Autowired
	private PublicationRepository publicationRepository;
	
	@Autowired
	private ReadBookService readBookService;

	@Autowired
	private AuthoritiesService authoritiesService;
	
	@Autowired
	public PublicationService(final PublicationRepository publicationRepository) {
		this.publicationRepository = publicationRepository;
	}
	
	@Transactional(readOnly = true)
	public Collection<Publication> findAllPublicationFromBook(final int id) throws DataAccessException {
		return this.publicationRepository.getAllPublicationsFromBook(id);
	}

	@Transactional(readOnly = true)
	public Publication findById(final int id) throws DataAccessException {
		return this.publicationRepository.findById(id);

	}
	
	@Transactional(readOnly = true)
	public List<Integer> getPublicationsFromBook(int bookId) throws DataAccessException {
		return this.publicationRepository.getPublicationsFromBook(bookId);
	}
	
	@Transactional
	public void save(final Publication publication) throws DataAccessException {
		this.publicationRepository.save(publication);
		
	}
	
	@Transactional
	@Modifying
	public void deletePublication(int publicationId) throws DataAccessException {
		this.publicationRepository.deletePublication(publicationId);
	}
	
	
	@Transactional
	public Boolean existsPublicationById(int publicationId) throws DataAccessException {
		return this.publicationRepository.existsById(publicationId);
	}

	
	public Boolean publicationMioOAdmin(int publicationId, String username) {
		Boolean mine = false;
		Boolean imAdmin = false;
		Publication publication = this.findById(publicationId);

		List<Authorities> authorities = this.authoritiesService.getAuthoritiesByUsername(username);
		for (Authorities a : authorities) {
			if (a.getAuthority().equals("admin")) {
				imAdmin = true;
			}
		}
		if (username.equals(publication.getUser().getUsername())) {
			mine = true;
		}

		return mine||imAdmin;
	}
	
	public Boolean publicationMio(int publicationId, String username) {
		Boolean mine = false;
		Publication publication = this.findById(publicationId);

		if (username.equals(publication.getUser().getUsername())) {
			mine = true;
		}

		return mine;
	}
	
}
