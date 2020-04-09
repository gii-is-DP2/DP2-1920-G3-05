package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Publication;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PublicationServiceTests {
	
	@Autowired
	private PublicationService sut;

	@Autowired
	private UserService	userService;
	
	@Autowired
	private BookService	sut2;
	
	@ParameterizedTest
	@CsvSource({
		"1,2",
		"2,1",
		"9,0"
	})
	void shouldFindPublicationsIdByBookId(int bookId, int results) {
		List<Integer> publicationsId = this.sut.getPublicationsFromBook(bookId);
		Assertions.assertThat(publicationsId.size()).isEqualTo(results);
	}
	
	@ParameterizedTest
	@CsvSource({
		"1",
		"2",
		"3"
	})
	void shouldDeletePublication(int publicationId){
		this.sut.deletePublication(publicationId);
		Boolean exisitsPublication = this.sut.existsPublicationById(publicationId);
		Assertions.assertThat(exisitsPublication).isFalse();
	}
	
	
	@ParameterizedTest
	@CsvSource({
		"1,admin1,7",
		"2,owner1,8"
	})
	public void shouldInsertPublicationIntoDatabaseAndGenerateId(int bookId, String username, int futureId) throws DataAccessException {
		Collection<Publication> list = this.sut.findAllPublicationFromBook(bookId);
		User user = this.userService.findUserByUsername(username);
		Book book = this.sut2.findBookById(bookId);
		int count = list.size();

		Publication publication = new Publication();
		publication.setTitle("prueba");
		publication.setPublicationDate(LocalDate.now());
		publication.setDescription("Esto es una prueba");
		publication.setImage("https://los40es00.epimg.net/los40/imagenes/los40classic/2018/03/foto-test.png");
		publication.setUser(user);
		publication.setBook(book);

		this.sut.save(publication);

		Assertions.assertThat(futureId).isNotNull();
		list = this.sut.findAllPublicationFromBook(bookId);
		Assertions.assertThat(list.size()).isEqualTo(count + 1);

	}
	
	@ParameterizedTest
	@CsvSource({
		"1",
		"2"
	})
	public void shouldUpdatePublicationTitle(int publicationId) throws Exception {
		Publication publication = this.sut.findById(publicationId);
		String oldTitle = publication.getTitle();

		String newTitle = oldTitle + "X";
		publication.setTitle(newTitle);
		this.sut.save(publication);

		publication = this.sut.findById(publicationId);
		Assertions.assertThat(publication.getTitle()).isEqualTo(newTitle);
	}

	@ParameterizedTest
	@CsvSource({
		"owner1,2",
		"admin1,1",
		"owner1,4"
	})
	void publicationShouldBeMine(String username, int publicationId){
		Boolean isMine = this.sut.publicationMioOAdmin(publicationId, username);
		Assertions.assertThat(isMine).isTrue();
	}

	@ParameterizedTest
	@CsvSource({
		"admin1,2",
		"admin1,4"
	})
	void publicationShouldBeMineImAdmin(String username, int publicationId){
		Boolean isMine = this.sut.publicationMioOAdmin(publicationId, username);
		Assertions.assertThat(isMine).isTrue();
	}

	@ParameterizedTest
	@CsvSource({
		"vet1,2",
		"owner1,1"
	})
	void publicationShouldNotBeMine(String username, int publicationId){
		Boolean isMine = this.sut.publicationMioOAdmin(publicationId, username);
		Assertions.assertThat(isMine).isFalse();
	}
	
	@ParameterizedTest
	@CsvSource({
		"owner1,2",
		"admin1,1"
	})
	void publicationShouldBeMine2(String username, int publicationId) {
		Boolean esMio = this.sut.publicationMio(publicationId, username);
		Assertions.assertThat(esMio).isTrue();
	}
	
	@ParameterizedTest
	@CsvSource({
		"vet1,2",
		"owner1,5",
		
	})
	void publicationShouldNotBeMine2(String username, int publicationId) {
		Boolean esMio = this.sut.publicationMio(publicationId, username);
		Assertions.assertThat(esMio).isFalse();
	}

	
	
}
