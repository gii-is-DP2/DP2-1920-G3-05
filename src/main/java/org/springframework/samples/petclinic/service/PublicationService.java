package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Publication;
import org.springframework.samples.petclinic.repository.BookRepository;
import org.springframework.samples.petclinic.repository.PublicationRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedISBNException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PublicationService {

	@Autowired
	private PublicationRepository publicationRepository;
	
//	@Autowired
//	private ImageService imageService;
	
	
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
		//Antes de borrar la publicación hay que borrar sus imagenes
//		List<Integer> imagesId = this.imageService.getImagesFromPublication(publicationId);
//		if(imagesId!=null && !imagesId.isEmpty()) {
//			for(Integer i: imagesId) {
//				this.imageService.deleteImage(i);
//			}
//		}
		this.publicationRepository.deletePublication(publicationId);
	}
	
	
	@Transactional
	public Boolean existsPublicationById(int publicationId) throws DataAccessException {
		return this.publicationRepository.existsById(publicationId);
	}
}
