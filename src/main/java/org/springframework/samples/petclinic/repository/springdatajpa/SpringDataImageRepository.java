package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Image;
import org.springframework.samples.petclinic.repository.ImageRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SpringDataImageRepository extends CrudRepository<Image, Integer>, ImageRepository{

	@Override
	@Transactional
	@Query("SELECT image.id FROM Image image WHERE image.publication.id = ?1")
	public List<Integer> getImagesFromPublication(int publicationId);
	
	@Override
	@Transactional
	@Modifying
	@Query("DELETE FROM Image WHERE id = ?1")
	public void deleteImage(int imageId);
}
