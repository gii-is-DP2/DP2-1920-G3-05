package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface ImageRepository {

	@Transactional(readOnly = true)
	List<Integer> getImagesFromPublication(int publicationId) throws DataAccessException;
	
	@Transactional
	@Modifying
	void deleteImage(int imageId) throws DataAccessException;
	
	@Transactional(readOnly = true)
	Boolean existsById(int imageId) throws DataAccessException;
	
}
