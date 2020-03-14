package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImageService {

	@Autowired 
	private ImageRepository imageRepository;
	
	@Transactional(readOnly = true)
	public List<Integer> getImagesFromPublication(int publicationId)  throws DataAccessException {
		return this.imageRepository.getImagesFromPublication(publicationId);
	}
	
	@Transactional
	@Modifying
	public void deleteImage(int imageId)  throws DataAccessException {
		this.imageRepository.deleteImage(imageId);
	}
	
	@Transactional
	public Boolean existsImageById(int imageId) throws DataAccessException {
		return this.imageRepository.existsById(imageId);
	}
}
