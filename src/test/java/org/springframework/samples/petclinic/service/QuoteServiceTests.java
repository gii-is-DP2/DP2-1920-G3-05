package org.springframework.samples.petclinic.service;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Quote;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
 class QuoteServiceTests {

	@Autowired
	private QuoteService sut;
	
	
	@Test
	void shouldGetRandomQoute() {
		Quote quote = this.sut.getRandomQuote();
		Assertions.assertThat(quote).isNotNull();
		Assertions.assertThat(quote.getAuthor()).isNotEmpty();
		Assertions.assertThat(quote.getContent()).isNotEmpty();
	}
}
