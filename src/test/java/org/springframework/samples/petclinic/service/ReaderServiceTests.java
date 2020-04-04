
package org.springframework.samples.petclinic.service;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Reader;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedUsernameException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class ReaderServiceTests {

	@Autowired
	private ReaderService	readerService;
	
	@Autowired
	private BookService	bookService;


	@ParameterizedTest
	@CsvSource({
		"admin1,George","owner1,Betty"
	})
	void shouldFindReaderByUsername(String username, String firstname) {
		Reader reader = this.readerService.findReaderByUsername(username);
		Assertions.assertThat(reader.getFirstName()).isEqualTo(firstname);
	}

	@ParameterizedTest
	@CsvSource({
		"reader2,George","reader3,Betty"
	})
	public void shouldCreateReader(String username, String firstname) throws DataAccessException, DuplicatedUsernameException {
		Reader reader = new Reader();
		User user = new User();
		user.setUsername(username);
		user.setPassword("pass");
		user.setEnabled(true);
		reader.setUser(user);
		reader.setVerified(true);
		reader.setAddress("address");
		reader.setCity("city");
		reader.setFirstName(firstname);
		reader.setLastName("last name");
		reader.setTelephone("12356789");

		this.readerService.saveReader(reader);
		Reader reader2 = this.readerService.findReaderByUsername(username);
		Assertions.assertThat(reader2).isNotNull();
		Assertions.assertThat(reader2.getFirstName()).isEqualTo(firstname);

	}

	@ParameterizedTest
	@CsvSource({
		"admin1","reader"
	})
	public void shouldNotCreateReader(String username) throws DataAccessException, DuplicatedUsernameException {
		Reader reader = new Reader();
		User user = new User();
		user.setUsername(username);
		user.setPassword("pass");
		user.setEnabled(true);
		reader.setUser(user);
		reader.setVerified(true);
		reader.setAddress("address");
		reader.setCity("city");
		reader.setFirstName("firstname");
		reader.setLastName("last name");
		reader.setTelephone("12356789");
		try {
			this.readerService.saveReader(reader);
		} catch (DuplicatedUsernameException e) {
			Assertions.assertThat(e.getCause());
		}

	}

	@ParameterizedTest
	@CsvSource({
		"admin1,First name","reader1, First name 2"
	})
	public void shouldUpdateReader(String username, String firstname) throws DataAccessException, DuplicatedUsernameException {
		Reader reader = this.readerService.findReaderByUsername(username);
		reader.setAddress("address");
		reader.setCity("city");
		reader.setFirstName(firstname);
		reader.setLastName("last name");
		reader.setTelephone("12356789");

		this.readerService.saveReader(reader);
		Reader reader2 = this.readerService.findReaderByUsername(username);
		Assertions.assertThat(reader2.getFirstName()).isEqualTo(firstname);
	}
	
	@Test
	void shouldVerifyReader() {
		this.readerService.verifyUser(2);
		Reader reader = this.readerService.findReaderByUsername("owner1");
		Assertions.assertThat(reader.getVerified()).isTrue();
		List<Boolean> verified= this.bookService.getVerifiedFromBooksByUsername(reader.getUser().getUsername());
		Assertions.assertThat(verified).allMatch(i->i==true);
	}

}
