package org.springframework.samples.petclinic.web;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.repository.BookRepository;
import org.springframework.samples.petclinic.repository.ReaderRepository;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.samples.petclinic.service.ReaderService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = AdminController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class AdminControllerTests {

	@MockBean
	private UserService				userservice;

	@Autowired
	private MockMvc					mockMvc;
	
	@MockBean
	private BookService				bookService;
	
	@MockBean
	private BookRepository	bookRepo;
	
	@MockBean
	private ReaderService				readerService;
	
	@MockBean
	private ReaderRepository	readerRepo;
	
	@WithMockUser(value = "spring")
	@Test
	void testShowReadersList() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/listUsers")).andExpect(status().isOk()).andExpect(model().attributeExists("selections"))
		.andExpect(view().name("users/listReaders"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testverifyUser() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/users/{userId}/verify",2).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(status().is3xxRedirection())
	.andExpect(redirectedUrl("/admin/listUsers"));
	}
}
