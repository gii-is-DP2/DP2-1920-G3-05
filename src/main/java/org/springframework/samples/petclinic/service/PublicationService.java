package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.repository.PublicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PublicationService {

	@Autowired
	private PublicationRepository publicationRepository;
	
	@Autowired
	private ImageService imageService;
	
	@Transactional(readOnly = true)
	public List<Integer> getPublicationsFromBook(int bookId) throws DataAccessException {
		return this.publicationRepository.getPublicationsFromBook(bookId);
	}
	
	@Transactional
	@Modifying
	public void deletePublication(int publicationId) throws DataAccessException {
		//Antes de borrar la publicación hay que borrar sus imagenes
		List<Integer> imagesId = this.imageService.getImagesFromPublication(publicationId);
		if(imagesId!=null && !imagesId.isEmpty()) {
			for(Integer i: imagesId) {
				this.imageService.deleteImage(i);
			}
		}
		this.publicationRepository.deletePublication(publicationId);
	}
	
	@Transactional
	public Boolean existsPublicationById(int publicationId) throws DataAccessException {
		return this.publicationRepository.existsById(publicationId);
	}
}
