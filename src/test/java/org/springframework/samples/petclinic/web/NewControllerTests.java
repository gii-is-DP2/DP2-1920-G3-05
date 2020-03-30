
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.New;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.samples.petclinic.service.NewService;
import org.springframework.samples.petclinic.service.exceptions.CantDeleteBookInNewException;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = NewController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class NewControllerTests {

	private static final int	TEST_BOOK_ID	= 1;

	private static final int	TEST_NEW_ID		= 4;

	private static final int	TEST_NEW_ID_2	= 5;

	private static final int	TEST_NEW_ID_3	= 6;

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private NewService			newService;

	@MockBean
	private BookService			bookService;


	@BeforeEach
	void setup() throws CantDeleteBookInNewException {
		Mockito.when(this.bookService.findAll()).thenReturn(new ArrayList<>());

		New neew = new New();
		neew.setId(NewControllerTests.TEST_NEW_ID);
		neew.setName("Test title");
		neew.setBody("Test body");
		LocalDate fecha = LocalDate.of(2010, 03, 12);
		neew.setFecha(fecha);
		neew.setHead("Test head");
		neew.setImg("https://1276982291_248063_1440x600.jpg");
		neew.setRedactor("Test redactor");
		neew.setTags("Test tags");

		New neew2 = new New();
		neew2.setId(NewControllerTests.TEST_NEW_ID_2);
		neew2.setName("Test title 2");
		neew2.setBody("Test body 2");
		LocalDate fecha2 = LocalDate.of(2015, 05, 07);
		neew2.setFecha(fecha2);
		neew2.setHead("Test head 2");
		neew2.setImg("https://T_1276982291_248063_1440x600.jpg");
		neew2.setRedactor("Test redactor 2");
		neew2.setTags("Test tags 2");

		Mockito.when(this.newService.getNewById(NewControllerTests.TEST_NEW_ID)).thenReturn(neew);
		Mockito.when(this.newService.getBooksFromNews(NewControllerTests.TEST_NEW_ID_3)).thenReturn(new ArrayList<Book>());
		List<Book> booksNews1 = new ArrayList<Book>();
		booksNews1.add(new Book());
		Mockito.when(this.newService.getBooksFromNews(NewControllerTests.TEST_NEW_ID)).thenReturn(booksNews1);
		List<Book> booksNews = new ArrayList<Book>();
		booksNews.add(new Book());
		booksNews.add(new Book());
		Mockito.when(this.newService.getBooksFromNews(NewControllerTests.TEST_NEW_ID_2)).thenReturn(booksNews);

		Mockito.doCallRealMethod().when(this.newService).deleteBookInNew(NewControllerTests.TEST_NEW_ID, NewControllerTests.TEST_BOOK_ID);//Necesitamos que entre en el metodo para que lance la excepcion
	}

	@WithMockUser(value = "spring")
	@Test
	void testAllNews() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/news")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("NewsRec")).andExpect(MockMvcResultMatchers.model().attributeExists("news"))
			.andExpect(MockMvcResultMatchers.view().name("news/newList"));
	}

	@WithMockUser(value = "spring", authorities = "ROLE_ANONYMOUS")
	@Test
	void testAllNewsAnónimo() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/news")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("NewsRec")).andExpect(MockMvcResultMatchers.model().attributeExists("news"))
			.andExpect(MockMvcResultMatchers.view().name("news/newList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowNew() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/news/{newId}", NewControllerTests.TEST_NEW_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("new"))
			.andExpect(MockMvcResultMatchers.model().attribute("new", Matchers.hasProperty("name", Matchers.is("Test title")))).andExpect(MockMvcResultMatchers.model().attribute("new", Matchers.hasProperty("body", Matchers.is("Test body"))))
			.andExpect(MockMvcResultMatchers.model().attribute("new", Matchers.hasProperty("redactor", Matchers.is("Test redactor")))).andExpect(MockMvcResultMatchers.view().name("news/createOrUpdateNewForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSaveNew() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/admin/news/{newId}", NewControllerTests.TEST_NEW_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Edited name").param("body", "Edited body").param("redactor", "Edited redactor")
			.param("fecha", "2010/05/12").param("head", "Edited head").param("img", "https://1276982291_248063_1440x600.jpg")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.redirectedUrl("/news"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSaveNewWithErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/admin/news/{newId}", NewControllerTests.TEST_NEW_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "").param("fecha", "2050/05/07").param("img", "aa"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("new")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("new", "name"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("new", "name")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("new", "fecha")).andExpect(MockMvcResultMatchers.view().name("news/createOrUpdateNewForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreateNew() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/news/create")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("new"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("addNew")).andExpect(MockMvcResultMatchers.view().name("news/createOrUpdateNewForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testCreateNew() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/admin/news/create").with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Edited name").param("body", "Edited body").param("redactor", "Edited redactor").param("fecha", "2010/05/12")
			.param("head", "Edited head").param("img", "https://1276982291_248063_1440x600.jpg")).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testCreateNewWithErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/admin/news/create").with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "").param("fecha", "2050/05/07").param("img", "aa")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("new")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("new", "name")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("new", "name"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("new", "fecha")).andExpect(MockMvcResultMatchers.view().name("news/createOrUpdateNewForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testDeleteNew() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/news/delete/{newId}", NewControllerTests.TEST_NEW_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.redirectedUrl("/news"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testBooksNew() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/news/books/{newId}", NewControllerTests.TEST_NEW_ID)).andExpect(MockMvcResultMatchers.model().attributeExists("booksIncludes"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("booksNotIncludes")).andExpect(MockMvcResultMatchers.view().name("news/bookList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testBooksNewSave() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/news/books/save/{newId}", NewControllerTests.TEST_NEW_ID_2)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.redirectedUrl("/news"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testBooksNewSaveEmpty() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/news/books/save/{newId}", NewControllerTests.TEST_NEW_ID_3)).andExpect(MockMvcResultMatchers.model().attributeExists("booksIncludes"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("booksNotIncludes")).andExpect(MockMvcResultMatchers.model().attributeExists("booksNotEmpty")).andExpect(MockMvcResultMatchers.view().name("news/bookList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testDeleteBooksFromNew() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/news/books/delete/{newId}/{bookId}", NewControllerTests.TEST_NEW_ID_2, NewControllerTests.TEST_BOOK_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/admin/news/books/" + NewControllerTests.TEST_NEW_ID_2));
	}

	@WithMockUser(value = "spring")
	@Test
	void testDeleteBooksFromNewError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/news/books/delete/{newId}/{bookId}", NewControllerTests.TEST_NEW_ID, NewControllerTests.TEST_BOOK_ID)).andExpect(MockMvcResultMatchers.model().attributeExists("booksIncludes"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("booksNotIncludes")).andExpect(MockMvcResultMatchers.model().attributeExists("booksNotEmpty")).andExpect(MockMvcResultMatchers.view().name("news/bookList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSaveBooksInNew() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/news/books/add/{newId}/{bookId}", NewControllerTests.TEST_NEW_ID, NewControllerTests.TEST_BOOK_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/admin/news/books/" + NewControllerTests.TEST_NEW_ID));
	}

}
