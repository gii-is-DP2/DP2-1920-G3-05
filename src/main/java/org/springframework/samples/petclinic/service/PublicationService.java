package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.repository.PublicationRepository;

public class PublicationService {

	@Autowired
	private PublicationRepository publicationService;
}
