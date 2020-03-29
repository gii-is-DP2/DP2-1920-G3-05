
package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Reader;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.ReaderService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedUsernameException;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = UserController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)

public class UserControllerTests {

	@Autowired
	private MockMvc			mockMvc;

	@MockBean
	private ReaderService	readerService;


	@BeforeEach
	void setup() throws DuplicatedUsernameException {
		User user = new User();
		user.setUsername("username");
		user.setPassword("pass");
		Reader reader = new Reader();
		reader.setId(1);
		reader.setAddress("Address");
		reader.setCity("City");
		reader.setFirstName("First name");
		reader.setLastName("Last name");
		reader.setTelephone("12345678");
		reader.setVerified(false);
		reader.setUser(user);

		User user2 = new User();
		user2.setUsername("username");
		user2.setPassword("pass");
		Reader reader2 = new Reader();
		reader2.setId(2);
		reader2.setAddress("Address");
		reader2.setCity("City");
		reader2.setFirstName("First name");
		reader2.setLastName("Last name");
		reader2.setTelephone("12345678");
		reader2.setVerified(false);
		reader2.setUser(user);

		Mockito.when(this.readerService.findReaderByUsername("username")).thenReturn(reader2);
		Mockito.doCallRealMethod().when(this.readerService).saveReader(reader);//Necesitamos que entre en el metodo para que lance la excepcion

	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreateReader() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("reader"))
			.andExpect(MockMvcResultMatchers.view().name("users/createReaderForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testCreateReaderErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("city", "Test city").param("telephone", "12234567").param("user.username", "").param("user.password", ""))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("reader", "address")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("reader", "firstName"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("reader", "lastName")).andExpect(MockMvcResultMatchers.view().name("users/createReaderForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testCreateReader() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("city", "Test city").param("telephone", "12234567").param("user.username", "username").param("user.password", "pass")
			.param("address", "address").param("firstName", "first name").param("lastName", "last name")).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(value = "spring", username = "username")
	@Test
	void testInitUpdateReader() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/update")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("reader"))
			.andExpect(MockMvcResultMatchers.view().name("users/updateReaderForm"));
	}

	@WithMockUser(value = "spring", username = "username")
	@Test
	void testUpdateReader() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/update").with(SecurityMockMvcRequestPostProcessors.csrf()).param("city", "Test city").param("telephone", "12234567").param("user.password", "pass").param("address", "address")
			.param("firstName", "first name").param("lastName", "last name")).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(value = "spring", username = "username")
	@Test
	void testUpdateReaderErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/update").with(SecurityMockMvcRequestPostProcessors.csrf()).param("city", "Test city").param("telephone", "12234567").param("user.password", "").param("address", "address"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("reader", "firstName")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("reader", "lastName"))
			.andExpect(MockMvcResultMatchers.view().name("users/updateReaderForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testCreateReaderDuplicatedUsername() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/users/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("verified", "false").param("city", "City").param("telephone", "12345678").param("user.username", "username")
				.param("user.password", "pass").param("address", "Address").param("firstName", "First name").param("lastName", "Last name").param("id", "1"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("reader", "user.username")).andExpect(MockMvcResultMatchers.view().name("users/createReaderForm"));
	}
}
