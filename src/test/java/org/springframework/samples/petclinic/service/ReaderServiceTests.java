
package org.springframework.samples.petclinic.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
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
	private UserService		userService;

	@Autowired
	private ReaderService	readerService;


	@Test
	void shouldFindReaderByUsername() {
		Reader reader = this.readerService.findReaderByUsername("admin1");
		Assertions.assertThat(reader.getFirstName()).isEqualTo("George");
	}

	@Test
	public void shouldCreateReader() throws DataAccessException, DuplicatedUsernameException {
		Reader reader = new Reader();
		User user = new User();
		user.setUsername("reader");
		user.setPassword("pass");
		user.setEnabled(true);
		reader.setUser(user);
		reader.setVerified(true);
		reader.setAddress("address");
		reader.setCity("city");
		reader.setFirstName("first name");
		reader.setLastName("last name");
		reader.setTelephone("12356789");

		this.readerService.saveReader(reader);
		Reader reader2 = this.readerService.findReaderByUsername("reader");
		Assertions.assertThat(reader2).isNotNull();
		Assertions.assertThat(reader2.getFirstName()).isEqualTo("first name");

	}

	@Test
	public void shouldNotCreateReader() throws DataAccessException, DuplicatedUsernameException {
		Reader reader = new Reader();
		User user = new User();
		user.setUsername("admin1");
		user.setPassword("pass");
		user.setEnabled(true);
		reader.setUser(user);
		reader.setVerified(true);
		reader.setAddress("address");
		reader.setCity("city");
		reader.setFirstName("first name");
		reader.setLastName("last name");
		reader.setTelephone("12356789");
		try {
			this.readerService.saveReader(reader);
		} catch (DuplicatedUsernameException e) {
			Assertions.assertThat(e.getCause());
		}

	}

	@Test
	public void shouldUpdateReader() throws DataAccessException, DuplicatedUsernameException {
		Reader reader = this.readerService.findReaderByUsername("owner1");
		reader.setAddress("address");
		reader.setCity("city");
		reader.setFirstName("first name");
		reader.setLastName("last name");
		reader.setTelephone("12356789");

		this.readerService.saveReader(reader);
		Reader reader2 = this.readerService.findReaderByUsername("owner1");
		Assertions.assertThat(reader2.getFirstName()).isEqualTo("first name");
	}

}
