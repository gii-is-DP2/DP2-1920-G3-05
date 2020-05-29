package org.springframework.samples.petclinic.integration.DB;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Publication;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.BookRepository;
import org.springframework.samples.petclinic.repository.PublicationRepository;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("mysql") 
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class)) 
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class PublicationsIntegrationTest {
	
	@Autowired
	private PublicationRepository publicationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	
	@ParameterizedTest
	@CsvSource({
		"1,admin1,prueba",
		"2,owner1,prueba2"
	})
	public void shouldCreatePublication(int bookId, String username, String title)  {
		User user = this.userRepository.findByUsername(username);
		Book book = this.bookRepository.findById(bookId);

		Publication publication = new Publication();
		publication.setTitle(title);
		publication.setPublicationDate(LocalDate.now());
		publication.setDescription("Esto es una prueba");
		publication.setImage("https://los40es00.epimg.net/los40/imagenes/los40classic/2018/03/foto-test.png");
		publication.setUser(user);
		publication.setBook(book);

		this.publicationRepository.save(publication);

		Assertions.assertThat(this.publicationRepository.findById(publication.getId()).getBook().getId()).isEqualTo(bookId);
		Assertions.assertThat(this.publicationRepository.findById(publication.getId()).getTitle()).isEqualTo(title);
		Assertions.assertThat(this.publicationRepository.findById(publication.getId()).getUser().getUsername()).isEqualTo(username);

	}
	
	@ParameterizedTest
	@CsvSource({
		"1,true","3,true","12,false"
	})
	public void shouldGetExistPublicationById(int publicationId,boolean result) {
		Assertions.assertThat(this.publicationRepository.existsById(publicationId)).isEqualTo(result);
	}
	
	@ParameterizedTest
	@CsvSource({
		"1,admin1,1","4,owner1,7"
	})
	public void shouldFindPublicationById(int publicationId,String username,int bookId) {
		Publication publication = this.publicationRepository.findById(publicationId);
		Assertions.assertThat(publication.getBook().getId()).isEqualTo(bookId);
		Assertions.assertThat(publication.getUser().getUsername()).isEqualTo(username);
	}
	
	@ParameterizedTest
	@CsvSource({
		"2,1","1,2","0,5"
	})
	public void shouldFindAllPublicationById(int result,int bookId) {
		List<Publication> listPublication = (List<Publication>) this.publicationRepository.getAllPublicationsFromBook(bookId);
		Assertions.assertThat(listPublication.size()).isEqualTo(result);
	}
	
	@ParameterizedTest
	@CsvSource({
		"Test title update 1,2","Test title update 2,5"
	})
	public void shouldUpdatePublication(String title,int publicationId) {
		Publication publication = this.publicationRepository.findById(publicationId);
		publication.setTitle(title);
		this.publicationRepository.save(publication);
		Assertions.assertThat(this.publicationRepository.findById(publicationId).getTitle()).isEqualTo(title);
	}
	
	@ParameterizedTest
	@CsvSource({
		"3","6"
	})
	public void shouldDeletePublicationById(int publicationId) {
		this.publicationRepository.deletePublication(publicationId);
		Assertions.assertThat(this.publicationRepository.findById(publicationId)).isNull();
	}
	
	

}
