package org.springframework.samples.petclinic.web.e2e;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.hamcrest.Matchers;
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
public class NewControllerTestsE2E {
	
	private static final int TEST_BOOK_ID = 1;
	private static final int TEST_NEW_ID = 4;
	private static final int TEST_NEW_ID2 = 2;
	private static final int TEST_NEW_ID3 = 5;
	private static final int TEST_NEW_ID4 = 7;
	private static final int TEST_NEW_ID5 = 1;
	
	@Autowired
	private MockMvc mockMvc;
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testAllNews() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/news"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("NewsRec"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("news"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("canShowNewsBookReview"))
		.andExpect(MockMvcResultMatchers.view().name("news/newList"));
	}
	
	@WithMockUser(value = "spring", authorities = "ROLE_ANONYMOUS")
	@Test
	void testAllNewsAnónimo() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/news"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("NewsRec"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("canShowNewsBookReview"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("news"))
			.andExpect(MockMvcResultMatchers.view().name("news/newList"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testShowNew() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/news/{newId}", TEST_NEW_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("new"))
		.andExpect(MockMvcResultMatchers.model().attribute("new", Matchers.hasProperty("name", Matchers.is("Noticia 4"))))
		.andExpect(MockMvcResultMatchers.model().attribute("new", Matchers.hasProperty("body", Matchers.is("La nueva novela de Julia Navarro, «Tú no matarás» (Plaza & Janés y en catalán, en Rosa dels Vents), se publicará el próximo 25 de octubre."))))
		.andExpect(MockMvcResultMatchers.model().attribute("new", Matchers.hasProperty("redactor", Matchers.is("Jhon Doe"))))
		.andExpect(MockMvcResultMatchers.view().name("news/createOrUpdateNewForm"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testSaveNew() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/admin/news/{newId}", TEST_NEW_ID2)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Edited name")
				.param("body", "Edited body")
				.param("redactor", "Edited redactor")
				.param("fecha", "2010/05/12")
				.param("head", "Edited head")
				.param("img", "https://1276982291_248063_1440x600.jpg"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.redirectedUrl("/news"));
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testSaveNewWithErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/admin/news/{newId}", TEST_NEW_ID2)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "")
				.param("fecha", "2050/05/07").param("img", "aa"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("new"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("new", "name"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("new", "name"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("new", "fecha"))
			.andExpect(MockMvcResultMatchers.view().name("news/createOrUpdateNewForm"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testShowNewBookReviewHtml() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/news/newsbookreview"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("news"))
		.andExpect(MockMvcResultMatchers.model().attribute("AllNews", Matchers.is(true)))
		.andExpect(MockMvcResultMatchers.view().name("news/newList"));
	}
	
	@WithMockUser(value = "spring", authorities = "ROLE_ANONYMOUS")
	@Test
	void testShowNewAnonymousHtml() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/news"));
	}
	
	@WithMockUser(username="reader2",authorities= {"reader"})
	@Test
	void testShowNewWithoutReviewHtml() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/news"));
	}
	
	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testShowNewsWithReviewtHtml() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/")).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("news"))
		.andExpect(MockMvcResultMatchers.model().attribute("AllNews", Matchers.is(true)))
		.andExpect(MockMvcResultMatchers.view().name("news/newList"));
	}
	
	
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testBooksNew() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/news/books/{newId}", TEST_NEW_ID))
		.andExpect(MockMvcResultMatchers.model().attributeExists("booksIncludes"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("booksNotIncludes"))
			.andExpect(MockMvcResultMatchers.view().name("news/bookList"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testBooksNewSave() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/news/books/save/{newId}", TEST_NEW_ID3))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.redirectedUrl("/news"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testBooksNewSaveEmpty() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/news/books/save/{newId}", TEST_NEW_ID4))
		.andExpect(MockMvcResultMatchers.model().attributeExists("booksIncludes"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("booksNotIncludes"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("booksNotEmpty"))
		.andExpect(MockMvcResultMatchers.view().name("news/bookList"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testDeleteBooksFromNew() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/news/books/delete/{newId}/{bookId}", TEST_NEW_ID3, TEST_BOOK_ID))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.redirectedUrl("/admin/news/books/" + TEST_NEW_ID3));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testDeleteBooksFromNewError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/news/books/delete/{newId}/{bookId}", TEST_NEW_ID2, TEST_BOOK_ID))
		.andExpect(MockMvcResultMatchers.model().attributeExists("booksIncludes"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("booksNotIncludes"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("booksNotEmpty"))
		.andExpect(MockMvcResultMatchers.view().name("news/bookList"));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testSaveBooksInNew() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/news/books/add/{newId}/{bookId}", TEST_NEW_ID, TEST_BOOK_ID))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.redirectedUrl("/admin/news/books/" + TEST_NEW_ID));
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testInitCreateNew() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/news/create"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("new"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("addNew"))
		.andExpect(MockMvcResultMatchers.view().name("news/createOrUpdateNewForm"));
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testCreateNew() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/admin/news/create")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "Edited name")
				.param("body", "Edited body")
				.param("redactor", "Edited redactor")
				.param("fecha", "2010/05/12")
				.param("head", "Edited head")
				.param("img", "https://1276982291_248063_1440x600.jpg"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
	
	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testCreateNewWithErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/admin/news/create")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "").param("fecha", "2050/05/07")
				.param("img", "aa"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("new"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("new", "name"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("new", "name"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("new", "fecha"))
			.andExpect(MockMvcResultMatchers.view().name("news/createOrUpdateNewForm"));
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testDeleteNew() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/news/delete/{newId}", TEST_NEW_ID5))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.redirectedUrl("/news"));
	}
	
	
	

	
	
	
	

}
