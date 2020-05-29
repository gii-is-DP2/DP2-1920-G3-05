package org.springframework.samples.petclinic.integration.DB;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Publication;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.BookRepository;
import org.springframework.samples.petclinic.repository.PublicationRepository;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.stereotype.Service;

@ActiveProfiles("mysql") 
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class)) 
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
 class PublicationsIntegrationTest {
	
	@Autowired
	private PublicationRepository ationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	
	@ParameterizedTest
	@CsvSource({
		"1,admin1,prueba",
		"2,owner1,prueba2"
	})
	 void shouldCreatePublication(int bookId, String username, String title) throws DataAccessException {
		User user = this.userRepository.findByUsername(username);
		Book book = this.bookRepository.findById(bookId);

		Publication ation = new Publication();
		ation.setTitle(title);
		ation.setPublicationDate(LocalDate.now());
		ation.setDescription("Esto es una prueba");
		ation.setImage("https://los40es00.epimg.net/los40/imagenes/los40classic/2018/03/foto-test.png");
		ation.setUser(user);
		ation.setBook(book);

		this.ationRepository.save(ation);

		Assertions.assertThat(this.ationRepository.findById(ation.getId()).getBook().getId()).isEqualTo(bookId);
		Assertions.assertThat(this.ationRepository.findById(ation.getId()).getTitle()).isEqualTo(title);
		Assertions.assertThat(this.ationRepository.findById(ation.getId()).getUser().getUsername()).isEqualTo(username);

	}
	
	@ParameterizedTest
	@CsvSource({
		"1,true","3,true","12,false"
	})
	 void shouldGetExistPublicationById(int ationId,boolean result) {
		Assertions.assertThat(this.ationRepository.existsById(ationId)).isEqualTo(result);
	}
	
	@ParameterizedTest
	@CsvSource({
		"1,admin1,1","4,owner1,7"
	})
	 void shouldFindPublicationById(int ationId,String username,int bookId) {
		Publication ation = this.ationRepository.findById(ationId);
		Assertions.assertThat(ation.getBook().getId()).isEqualTo(bookId);
		Assertions.assertThat(ation.getUser().getUsername()).isEqualTo(username);
	}
	
	@ParameterizedTest
	@CsvSource({
		"2,1","1,2","0,5"
	})
	 void shouldFindAllPublicationById(int result,int bookId) {
		List<Publication> listPublication = (List<Publication>) this.ationRepository.getAllPublicationsFromBook(bookId);
		Assertions.assertThat(listPublication.size()).isEqualTo(result);
	}
	
	@ParameterizedTest
	@CsvSource({
		"Test title update 1,2","Test title update 2,5"
	})
	 void shouldUpdatePublication(String title,int ationId) {
		Publication ation = this.ationRepository.findById(ationId);
		ation.setTitle(title);
		this.ationRepository.save(ation);
		Assertions.assertThat(this.ationRepository.findById(ationId).getTitle()).isEqualTo(title);
	}
	
	@ParameterizedTest
	@CsvSource({
		"3","6"
	})
	 void shouldDeletePublicationById(int ationId) {
		this.ationRepository.deletePublication(ationId);
		Assertions.assertThat(this.ationRepository.findById(ationId)).isNull();
	}
	
	

}
