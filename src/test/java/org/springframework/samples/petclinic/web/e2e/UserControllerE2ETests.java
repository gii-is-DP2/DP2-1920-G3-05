
package org.springframework.samples.petclinic.web.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
  webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
 class UserControllerE2ETests {

	@Autowired
	private MockMvc			mockMvc;

	
	@Test
	void testInitCreateReader() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("reader"))
			.andExpect(MockMvcResultMatchers.view().name("users/createReaderForm"));
	}

	
	@Test
	void testCreateReaderErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("city", "Test city").param("telephone", "12234567").param("user.username", "").param("user.password", ""))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("reader", "address")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("reader", "firstName"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("reader", "lastName")).andExpect(MockMvcResultMatchers.view().name("users/createReaderForm"));
	}

	
	@Test
	void testCreateReader() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("city", "Test city").param("telephone", "12234567").param("user.username", "username").param("user.password", "pass")
			.param("address", "address").param("firstName", "first name").param("lastName", "last name")).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = "reader1", authorities = {"reader"})
	@Test
	void testInitUpdateReader() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/update")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("reader"))
			.andExpect(MockMvcResultMatchers.view().name("users/updateReaderForm"));
	}

	@WithMockUser(username = "reader1", authorities = {"reader"})
	@Test
	void testUpdateReader() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/update").with(SecurityMockMvcRequestPostProcessors.csrf()).param("city", "Test city").param("telephone", "12234567").param("user.password", "pass").param("address", "address")
			.param("firstName", "first name").param("lastName", "last name")).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = "reader1", authorities = {"reader"})
	@Test
	void testUpdateReaderErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users/update").with(SecurityMockMvcRequestPostProcessors.csrf()).param("city", "Test city").param("telephone", "12234567").param("user.password", "").param("address", "address"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("reader", "firstName")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("reader", "lastName"))
			.andExpect(MockMvcResultMatchers.view().name("users/updateReaderForm"));
	}

	
	@Test
	void testCreateReaderDuplicatedUsername() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/users/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("verified", "false").param("city", "City").param("telephone", "12345678").param("user.username", "username")
				.param("user.password", "pass").param("address", "Address").param("firstName", "First name").param("lastName", "Last name").param("id", "1"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("reader", "user.username")).andExpect(MockMvcResultMatchers.view().name("users/createReaderForm"));
	}
}
