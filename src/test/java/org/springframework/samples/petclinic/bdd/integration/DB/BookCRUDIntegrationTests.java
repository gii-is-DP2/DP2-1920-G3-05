package org.springframework.samples.petclinic.bdd.integration.DB;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Genre;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.BookRepository;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-mysql.properties")
public class BookCRUDIntegrationTests {
	@Autowired
    private BookRepository sut;
	@Autowired
    private UserRepository userRepo;

    @ParameterizedTest
	@CsvSource({
		"prueba,antonio,SM,9788425223280,574,Esto es una prueba,admin1,15","prueba2,pepe,Casals,9781234567897,123,Esto es otra prueba,admin1,16"
	})
    public void shouldCreateBook(String title,String author,String editorial,String isbn,Integer pages,String synopsis,String username,int size) {
    	User user = this.userRepo.findByUsername(username);
    	Book book = new Book();
		book.setTitle(title);
		book.setAuthor(author);
		book.setEditorial(editorial);
		book.setISBN(isbn);
		book.setPages(pages);
		book.setPublicationDate(LocalDate.now());
		Collection<Genre> genres = this.sut.findGenre();
		book.setGenre(EntityUtils.getById(genres, Genre.class, 3));
		book.setSynopsis(synopsis);
		book.setUser(user);

		this.sut.save(book);
		Integer booksSize= this.sut.findAll().size();
		Assertions.assertThat(size).isEqualTo(booksSize);
    }

    @ParameterizedTest
	@CsvSource({
		"1, IT",
		"2, Harry Potter y la piedra filosofal"
	})
	void shouldReadBook(int id, String title) {
		Book book = this.sut.findById(id);
		Assertions.assertThat(book.getTitle()).isEqualTo(title);
    }
    
    
    @ParameterizedTest
	@CsvSource({
		"14, 15",
		"13, 14",
	})
	void shouldDeleteBook(Integer bookId,Integer size){
		this.sut.deleteBookById(bookId);
		Integer booksSize= this.sut.findAll().size();
		Assertions.assertThat(size).isEqualTo(booksSize);
	}

}
