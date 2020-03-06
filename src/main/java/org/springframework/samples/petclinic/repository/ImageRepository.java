package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Image;

public interface ImageRepository extends CrudRepository<Image, Integer>{

}
