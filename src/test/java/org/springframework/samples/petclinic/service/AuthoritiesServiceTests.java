package org.springframework.samples.petclinic.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class AuthoritiesServiceTests {

	@Autowired
	private AuthoritiesService sut;
	
	@Test
	void shouldBeAdmin() {
		String username = "admin1";
		List<Authorities> authorities = this.sut.getAuthoritiesByUsername(username);
		org.assertj.core.api.Assertions.assertThat(authorities.get(0).getAuthority()).isEqualTo("admin");
	}
	
	@Test
	void shouldBeOwner() {
		String username = "owner1";
		List<Authorities> authorities = this.sut.getAuthoritiesByUsername(username);
		org.assertj.core.api.Assertions.assertThat(authorities.get(0).getAuthority()).isEqualTo("owner");
	}
	
	@Test
	void shouldBeVet() {
		String username = "vet1";
		List<Authorities> authorities = this.sut.getAuthoritiesByUsername(username);
		org.assertj.core.api.Assertions.assertThat(authorities.get(0).getAuthority()).isEqualTo("veterinarian");
	}
}
