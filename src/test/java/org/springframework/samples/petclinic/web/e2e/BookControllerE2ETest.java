
package org.springframework.samples.petclinic.web.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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
class BookControllerE2ETest {

	
	private static final int		TEST_BOOK_ID_1	= 4;
	private static final int		TEST_BOOK_ID_2	= 2;
	private static final int		TEST_BOOK_ID_3	= 3;
	

	@Autowired
	private MockMvc					mockMvc;

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testDelete() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/books/delete/{bookId}", BookControllerE2ETest.TEST_BOOK_ID_1)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.redirectedUrl("/books"));

	}
	
	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testInitUpdateBookForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/{bookId}/updateForm", BookControllerE2ETest.TEST_BOOK_ID_3)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("book"))
			.andExpect(MockMvcResultMatchers.model().attribute("book", Matchers.hasProperty("title", Matchers.is("Harry Potter y la camara secreta"))))
			.andExpect(MockMvcResultMatchers.model().attribute("book", Matchers.hasProperty("image", Matchers.is("https://imagessl9.casadellibro.com/a/l/t5/79/9788498382679.jpg"))))
			.andExpect(MockMvcResultMatchers.model().attribute("book", Matchers.hasProperty("verified", Matchers.is(false)))).andExpect(MockMvcResultMatchers.model().attributeExists("genres")).andExpect(MockMvcResultMatchers.view().name("books/UpdateBookForm"));

	}

	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testInitUpdateBookFormWithOtherUser() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/{bookId}/updateForm", BookControllerE2ETest.TEST_BOOK_ID_2)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.redirectedUrl("/oups"));

	}

	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testProcessUpdateBookFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/books/update/{bookId}", BookControllerE2ETest.TEST_BOOK_ID_3).with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "Change").param("author", "NewAuthor").param("editorial", "NewEdit")
				.param("genre.id", "3").param("ISBN", "9788408471608").param("pages", "12").param("synopsis", "nuevaSinopsis").param("image", "https://www.nombrevasiento.com/").param("publicationDate", "2018/11/11"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.redirectedUrl("/books/" + BookControllerE2ETest.TEST_BOOK_ID_3));
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessUpdateBookFormWithDuplicatedISBN() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/books/update/{bookId}", BookControllerE2ETest.TEST_BOOK_ID_2).with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "Change").param("author", "NewAuthor").param("editorial", "NewEdit")
				.param("genre.name", "fiction").param("ISBN", "9788498381498").param("pages", "12").param("synopsis", "nuevaSinopsis").param("image", "https://www.nombrevasiento.com/").param("publicationDate", "2018/11/11"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("books/UpdateBookForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateBookFormWithErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/books/update/{bookId}", BookControllerE2ETest.TEST_BOOK_ID_3).with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "").param("author", "NewAuthor").param("editorial", "NewEdit")
				.param("genre.name", "fiction").param("ISBN", "9780345805362").param("pages", "12").param("synopsis", "nuevaSinopsis").param("image", "https://www.nombrevasiento.com/").param("publicationDate", "2018/11/11"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("/books/UpdateBookForm"));
	}

	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testShowReadBooksListHtml() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/readBooks")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("selections"))
			.andExpect(MockMvcResultMatchers.view().name("books/booksList"));
	}
	
	@WithMockUser(username="vet1",authorities= {"vet"})
	@Test
	void testAddReadBook() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/readBooks/{bookId}", 7).with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("books/bookDetails"));
	}
	
	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testAddReadBookThatWasAdded() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/readBooks/{bookId}", BookControllerE2ETest.TEST_BOOK_ID_2).with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("errorReadBook", "you have already read the book!"))
			.andExpect(MockMvcResultMatchers.view().name("books/bookDetails"));
	}
	
	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testShowRecomendationsListWithReadBooksHtml() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/recomendations")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("selections"))
			.andExpect(MockMvcResultMatchers.model().attribute("notEmpty", Matchers.is(true))).andExpect(MockMvcResultMatchers.view().name("books/recomendationList"));
	}
	
	@WithMockUser(username="reader2",authorities= {"reader"})
	@Test
	void testShowRecomendationsListWithoutReadBooksHtml() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/recomendations")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("emptyy", Matchers.is(true)))
			.andExpect(MockMvcResultMatchers.view().name("books/recomendationList"));
	}

	@WithMockUser(username="admin1",authorities= {"admin1"})
	@Test
	void testShowRecomendationsListWithoutRecomendationsHtml() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/recomendations")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("NoMore", Matchers.is(true)))
			.andExpect(MockMvcResultMatchers.model().attribute("genreName", "horror")).andExpect(MockMvcResultMatchers.view().name("books/recomendationList"));
	}
	
	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testInitFindForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/find")).andExpect(status().isOk()).andExpect(model().attributeExists("book")).andExpect(model().attributeExists("poem")).andExpect(view().name("books/findBooks"));
	}
	
	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testProcessFindFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("selections")).andExpect(MockMvcResultMatchers.view().name("books/booksList"));
	}

	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testProcessFindForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books").param("title", "9788498382679")).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/books/" + BookControllerE2ETest.TEST_BOOK_ID_3));
	}

	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testProcessFindFormNoBooksFound() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books").param("title", "Unknown Title")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("book", "title"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("book", "title", "notFound")).andExpect(MockMvcResultMatchers.view().name("books/findBooks"));
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testShowBook() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/{bookId}", BookControllerE2ETest.TEST_BOOK_ID_2)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("book", Matchers.hasProperty("title", Matchers.is("Harry Potter y la piedra filosofal")))).andExpect(MockMvcResultMatchers.model().attribute("book", Matchers.hasProperty("author", Matchers.is("J.K. Rowling"))))
			.andExpect(MockMvcResultMatchers.model().attribute("book", Matchers.hasProperty("ISBN", Matchers.is("9788498382662")))).andExpect(MockMvcResultMatchers.view().name("books/bookDetails"));
	}

	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testProcessFindTopReadBooks() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/topRead")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("selections"))
			.andExpect(MockMvcResultMatchers.view().name("books/booksList"));
	}
	
	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testProcessListWishedBooks() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/wishList")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("selections"))
			.andExpect(MockMvcResultMatchers.view().name("books/booksList"));
	}
	
	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testProcessAddWishedBooks() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/books/wishList/{book.id}", BookControllerE2ETest.TEST_BOOK_ID_3).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/books"));
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessNotAddWishedBook() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/books/wishList/{book.id}", BookControllerE2ETest.TEST_BOOK_ID_3).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testVerifyBookAdmin() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/books/{bookId}/verify", BookControllerE2ETest.TEST_BOOK_ID_3)).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/books/" + BookControllerE2ETest.TEST_BOOK_ID_3));
	}

	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testAddBook() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/add")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("books/bookAdd")).andExpect(MockMvcResultMatchers.model().attributeExists("book"));
	}
	
	
	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testSaveBook() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/books/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "El nombre del viento").param("author", "Patrick").param("editorial", "SM")
				.param("genre.id", "22")
				.param("ISBN", "9780345805362").param("pages", "100")
				.param("synopsis",
					"He robado princesas a reyes agónicos. Incendié la ciudad de Trebon. He pasado la noche con Felurian y he despertado vivo y cuerdo. Me expulsaron de la Universidad a una edad a la que a la mayoría todavía no los dejan entrar. He recorrido de noche caminos de los que otros no se atreven a hablar ni siquiera de día. He hablado con dioses, he amado a mujeres y he escrito canciones que hacen llorar a los bardos. Me llamo Kvothe. Quizá hayas oído hablar de mí\"")
				.param("publicationDate", "2015/12/11").param("image", "https://pictures.abebooks.com/isbn/9780575081406-es.jpg")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.redirectedUrl("/books"));
		}

	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testSaveBookHasErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/books/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", " ").param("author", " ").param("editorial", " ").param("genre", " ").param("ISBN", " ").param("pages", "1")
				.param("synopsis", " ").param("publicationDate", "2015/11/12").param("image", ""))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("book")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("book", "title"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("book", "author")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("book", "editorial"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("book", "genre")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("book", "ISBN"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("book", "synopsis")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("book", "image")).andExpect(MockMvcResultMatchers.view().name("books/bookAdd"));
	}

	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test 
   void testSaveBookHasDuplicatedISBN() throws Exception { 
		 mockMvc.perform(post("/books/save") 
				   .with(csrf()) 
				   .param("title", "El nombre del viento") 
				   .param("author", "Patrick") 
				   .param("editorial", "SM") 
				   .param("genre.name", "Novela") 
				   .param("ISBN", "9788498381498") 
				   .param("pages", "100") 
				   .param("synopsis","He robado princesas a reyes agónicos. Incendié la ciudad de Trebon. He pasado la noche con Felurian y he despertado vivo y cuerdo. Me expulsaron de la Universidad a una edad a la que a la mayoría todavía no los dejan entrar. He recorrido de noche caminos de los que otros no se atreven a hablar ni siquiera de día. He hablado con dioses, he amado a mujeres y he escrito canciones que hacen llorar a los bardos. Me llamo Kvothe. Quizá hayas oído hablar de mí\"") 
				   .param("publicationDate","2015/12/11") 
				   .param("image","https://pictures.abebooks.com/isbn/9780575081406-es.jpg"))							 
				   .andExpect(status().isOk()) 
				   .andExpect(view().name("books/bookAdd")); 
   }
   
	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	void testTopLibrosMejorValorados() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/topRaited")).andExpect(status().isOk()).andExpect(model().attributeExists("selections"))
		.andExpect(model().attributeExists("raiting")).andExpect(view().name("books/topRaitedBooks"));

	}
}
