package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class WishedBookServiceTests {

	@Autowired
	private WishedBookService	sut;
	@Autowired
	private UserService	userService;
	
//	@Test
//	void shouldAddToWishList() {
//		User user= userService.findUserByUsername("admin1");
//	}
	
	@Test
	void shouldDeleteFromWishList() {
		sut.deleteByBookId(4);
		Collection<Integer> books=sut.findBooksIdByUser("admin1");
		Assertions.assertThat(books.size()).isEqualTo(1);
	}
}
