package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Genre;
import org.springframework.samples.petclinic.model.New;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.samples.petclinic.service.NewService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers=NewController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
class NewControllerTest {
	
	
	@Autowired
	private NewController newController;

	@MockBean
	private NewService newService;
	
	@MockBean
	private BookService			bookService;
	
	@MockBean
	private UserService			userService;

	@Autowired
	private MockMvc 			mockMvc;
	
	
	private New					new1;
	
	private New					new2;
	
	
	@BeforeEach
	void setup() {
		New new1 = new New();
		new1.setHead("Prueba");
		new1.setFecha(LocalDate.of(2019, 11, 12));
		new1.setBody("Esto es una prueba");
		new1.setImg("https://www.nombreviento.com/");
		new1.setRedactor("Juan");
		new1.setTags("#prueba");
		New new2 = new New();
		new2.setHead("Prueba1");
		new2.setFecha(LocalDate.of(2019, 12, 12));
		new2.setBody("Esto es una prueba 2");
		new2.setImg("https://www.nombreviento4.com/");
		new2.setRedactor("Pepe");
		new2.setTags("#prueb");
		

	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowNewBookReviewHtml() throws Exception {
		List<New> ids = new ArrayList<>();
		ids.add(this.new1);
		ids.add(this.new2);
		Mockito.when(this.newService.getNewsBookReview("spring")).thenReturn((Collection<New>)ids);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/news/newsbookreview")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("news"))
			.andExpect(MockMvcResultMatchers.model().attribute("AllNews", Matchers.is(true))).andExpect(MockMvcResultMatchers.view().name("news/newList"));
	}
	@WithMockUser(value = "spring", authorities = "ROLE_ANONYMOUS")
	@Test
	void testShowNewAnonymousHtml() throws Exception {

		Mockito.when(this.newService.getNewsBookReview("spring")).thenReturn(new ArrayList<>());
		this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/news"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowNewWithoutReviewHtml() throws Exception {

		Mockito.when(this.newService.getNewsBookReview("spring")).thenReturn(new ArrayList<>());
		this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/news"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowNewsWithReviewtHtml() throws Exception {
		List<New> ids = new ArrayList<>();
		ids.add(this.new1);
		ids.add(this.new2);
		Mockito.when(this.newService.getNewsBookReview("spring")).thenReturn((Collection<New>)ids);
		Mockito.when(this.newService.getNewsBookReview2("spring")).thenReturn((Collection<New>)ids);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("news"))
			.andExpect(MockMvcResultMatchers.model().attribute("AllNews", Matchers.is(true))).andExpect(MockMvcResultMatchers.view().name("news/newList"));
	}

}
