package org.springframework.samples.petclinic.integration.DB;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.ReadBook;
import org.springframework.samples.petclinic.repository.ReadBookRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.ComponentScan;

@ActiveProfiles("mysql")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class ReadBookQueriesIntegrationTests {

    @Autowired
    private ReadBookRepository sut;

    @ParameterizedTest
    @CsvSource({
        "vet1,4",
        "owner1,5",
        "reader1,6"
    })
    public void shouldFindReadBooksIdsByUsername(String username, int numberReadBooks) {
        List<Integer> readBooksIds = this.sut.getBooksIdByUsername(username);
        Assertions.assertThat(readBooksIds.size()).isEqualTo(numberReadBooks);
    }

    @ParameterizedTest
    @CsvSource({
        "reader1,2",
        "vet1,7",
        "admin1,4"
    })
    public void shouldGetReadBookByBookIdAndUsername(String username, int bookId) {
        //Si un usuario no se ha leido el libro debe dar null esta query
        ReadBook rb = this.sut.getReadBookByBookIdAndUsername(bookId, username);
        Assertions.assertThat(rb).isNull();
    }

    @Test
    public void shouldGetTopReadBooks() {
        List<Integer> topReadBooksIds = this.sut.getTopReadBooks();
        Assertions.assertThat(topReadBooksIds.size()).isEqualTo(10);
        Assertions.assertThat(topReadBooksIds.get(0)).isEqualTo(1);
        Assertions.assertThat(topReadBooksIds.get(1)).isEqualTo(6);

    }
}