package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Genre;
import org.springframework.samples.petclinic.model.Publication;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedISBNException;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PublicationServiceTests {
	
	@Autowired
	private PublicationService sut;
	
	@Autowired 
	private ImageService imageService;
	
	@Autowired
	private UserService	userService;
	
	@Autowired
	private BookService	sut2;
	
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
	
	@Test
	void shouldFindBookById() {
		Publication publication = this.sut.findById(1);

		Assertions.assertThat(publication.getId()).isEqualTo(1);
	}
	
	
	@Test
	@Transactional
	public void shouldInsertPublicationIntoDatabaseAndGenerateId() throws DataAccessException {
		Collection<Publication> list = this.sut.findAllPublicationFromBook(1);
		User user = this.userService.findUserByUsername("admin1");
		Book book = this.sut2.findBookById(1);
		int count = list.size();

		Publication publication = new Publication();
		publication.setTitle("prueba");
		publication.setPublicationDate(LocalDate.now());
		publication.setDescription("Esto es una prueba");
		publication.setImage("https://los40es00.epimg.net/los40/imagenes/los40classic/2018/03/foto-test.png");
		publication.setUser(user);
		publication.setBook(book);

		this.sut.save(publication);

		Assertions.assertThat(publication.getId()).isNotNull();
		list = this.sut.findAllPublicationFromBook(1);
		Assertions.assertThat(list.size()).isEqualTo(count + 1);

	}
	
	@Test
	@Transactional
	public void shouldUpdatePublicationTitle() throws Exception {
		Publication publication = this.sut.findById(1);
		String oldTitle = publication.getTitle();

		String newTitle = oldTitle + "X";
		publication.setTitle(newTitle);
		this.sut.save(publication);

		publication = this.sut.findById(1);
		Assertions.assertThat(publication.getTitle()).isEqualTo(newTitle);

	}
	
	
}
