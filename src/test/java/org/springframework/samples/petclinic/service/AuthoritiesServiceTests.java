package org.springframework.samples.petclinic.service;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
 class AuthoritiesServiceTests {

	@Autowired
	private AuthoritiesService sut;
	
	@ParameterizedTest
	@CsvSource({
		"admin1,admin",
		"owner1,owner",
		"vet1,veterinarian"
	})
	void shouldgetAuthorities(String username,String authority) {
		List<Authorities> authorities = this.sut.getAuthoritiesByUsername(username);
		org.assertj.core.api.Assertions.assertThat(authorities.get(0).getAuthority()).isEqualTo(authority);
	}
	
}
