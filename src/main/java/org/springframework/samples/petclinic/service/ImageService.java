package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.repository.ImageRepository;

public class ImageService {

	@Autowired 
	private ImageRepository imageRepository;
}
