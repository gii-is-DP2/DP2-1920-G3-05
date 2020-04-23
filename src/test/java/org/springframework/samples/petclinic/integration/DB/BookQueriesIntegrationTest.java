package org.springframework.samples.petclinic.integration.DB;

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
import org.springframework.samples.petclinic.repository.BookRepository;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-mysql.properties")
public class BookQueriesIntegrationTest {

    @Autowired
    private BookRepository sut;

    @ParameterizedTest
	@CsvSource({
		"harry,2","Julia,2","Novel,1","9788466345347,1","el,4","harrry,0"
	})
    public void shouldFindBooksByTitleAuthorGenre(String title,int size) {
        Collection<Book> books = this.sut.findBookByTitleAuthorGenreISBN(title);
		Assertions.assertThat(books.size()).isEqualTo(size);
    }

    @ParameterizedTest
	@CsvSource({
		"1, Fantasy",
		"4, Contemporary"
	})
	void shouldFindAllGenres(int genreId, String name) {
		Collection<Genre> bookGenre = this.sut.findGenre();
		Genre genre1 = EntityUtils.getById(bookGenre, Genre.class, genreId);
		Assertions.assertThat(genre1.getName()).isEqualTo(name);
    }
    
    @ParameterizedTest
	@CsvSource({
		"Romance", "Fiction", "Horror"
	})
	public void shouldFindGenre(String genre) throws Exception {
		Genre genre3 = this.sut.findGenreByName(genre);
		Assertions.assertThat(genre).isEqualTo(genre3.getName());
    }
    
    @Test
	void shouldHaveVerifiedBooks(){
		String username = "admin1";
		List<Boolean> verified= this.sut.getVerifiedFromBooksByUsername(username);
		Assertions.assertThat(verified).allMatch(i->i==true);
	}

}