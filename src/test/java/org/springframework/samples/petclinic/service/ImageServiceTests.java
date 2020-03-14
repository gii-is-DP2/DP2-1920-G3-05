package org.springframework.samples.petclinic.service;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ImageServiceTests {

	@Autowired
	private ImageService sut;
	
	@Test
	void shouldGetImagesIdByPublicationId() {
		int publicationId = 1;
		List<Integer> imagesId = this.sut.getImagesFromPublication(publicationId);
		Assertions.assertThat(imagesId.size()).isEqualTo(2);
		
		publicationId = 2;
		imagesId = this.sut.getImagesFromPublication(publicationId);
		Assertions.assertThat(imagesId.size()).isEqualTo(2);

		publicationId = 3;
		imagesId = this.sut.getImagesFromPublication(publicationId);
		Assertions.assertThat(imagesId.size()).isEqualTo(1);
		
		publicationId = 6;
		imagesId = this.sut.getImagesFromPublication(publicationId);
		Assertions.assertThat(imagesId).isEmpty();
	}
	
	@Test
	void shouldDeleteImage() {
		int imageId = 1;
		
		this.sut.deleteImage(imageId);
		
		Boolean existsImage = this.sut.existsImageById(imageId);
		Assertions.assertThat(existsImage).isFalse();
	}
	
}
