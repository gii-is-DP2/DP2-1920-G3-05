package org.springframework.samples.petclinic.service;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PublicationServiceTests {
	
	@Autowired
	private PublicationService sut;
	
	@Autowired 
	private ImageService imageService;
	
	@Test
	void shouldFindPublicationsIdByBookId() {
		int bookId = 1;
		List<Integer> publicationsId = this.sut.getPublicationsFromBook(bookId);
		Assertions.assertThat(publicationsId.size()).isEqualTo(2);
		
		bookId = 2;
		publicationsId = this.sut.getPublicationsFromBook(bookId);
		Assertions.assertThat(publicationsId.size()).isEqualTo(1);
		
		bookId = 9;
		publicationsId = this.sut.getPublicationsFromBook(bookId);
		Assertions.assertThat(publicationsId).isEmpty();
	}
	
	@Test
	void shouldDeletePublicationWithImages() {
		int publicationId = 1;
		
		List<Integer> imagesId = this.imageService.getImagesFromPublication(publicationId);
				
		this.sut.deletePublication(publicationId);
		Boolean exisitsPublication = this.sut.existsPublicationById(publicationId);
		Assertions.assertThat(exisitsPublication).isFalse();

		for(Integer i: imagesId) {
			Boolean existsImageInPublication = this.imageService.existsImageById(i);
			Assertions.assertThat(existsImageInPublication).isFalse();
		}
	}
	
	@Test
	void shouldDeletePublicationWithoutImages(){
		int publicationId = 6;
		
		this.sut.deletePublication(publicationId);
		
		Boolean exisitsPublication = this.sut.existsPublicationById(publicationId);
		Assertions.assertThat(exisitsPublication).isFalse();
	}
}
