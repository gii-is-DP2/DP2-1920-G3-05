package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.ItApiSearch;
import org.springframework.samples.petclinic.model.ItBookDetails;
import org.springframework.samples.petclinic.service.exceptions.BadIsbnException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ItApiServiceTests {

    @Autowired
    private ItApiService        sut;

    @ParameterizedTest
    @CsvSource({
        "mongo,10",
        "apache,10",
        "verilog,3"
    })
    void testSearchItBooks(String searchParam, int numberResults) {
        ItApiSearch search = this.sut.searchItBooks(searchParam);
        Assertions.assertThat(search.getItBooks().size()).isEqualTo(numberResults);
    }

    @ParameterizedTest
    @CsvSource({
        "9781118011034,Embedded SoPC Design with Nios II Processor and Verilog Examples",
        "9780321812186,Effective JavaScript",
        "9780321934505,Apache Hadoop YARN"
    })
    void getDetails(String isbn, String title) throws BadIsbnException {
        ItBookDetails itBookDetails = this.sut.getDetailsItBook(isbn);
        Assertions.assertThat(itBookDetails.getTitle()).isEqualTo(title);
    }

    @ParameterizedTest
    @CsvSource({
        "12","9781118011031","9781118011025"
    })
    void getDetailsBadIsbn(String isbn) throws BadIsbnException {
        assertThrows(BadIsbnException.class, () -> this.sut.getDetailsItBook(isbn));
    }

}