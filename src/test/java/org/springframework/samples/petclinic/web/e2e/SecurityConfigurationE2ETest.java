
package org.springframework.samples.petclinic.web.e2e;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
 class SecurityConfigurationE2ETest {

	@Autowired
	private MockMvc mockMvc;


	//estado 403 - no tiene permiso para acceder a '/' en este servidor, nos manda a oups

	@Test
	void testRedirectToLoginFromUsersUpdateNoAuthenticated() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/update"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(redirectedUrl("http://localhost/login"));
	}
    @WithMockUser(username = "reader1", authorities = {"reader"})
	@Test
	void testCanEnterUpdateReaderWithAuthenticated() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/update"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(view().name("users/updateReaderForm"));
    }
    @WithMockUser(username = "reader1", authorities = {"reader"})
	@Test
	void testDontHavePermissionToUsersNewWithAuthenticated() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/new"))
		.andExpect(MockMvcResultMatchers.status().is(403))
		.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
	@Test
	void testHavePermissionToUsersNewWithNoAuthenticated() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/new"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(view().name("users/createReaderForm"));
    }
    @WithMockUser(username = "admin1", authorities = {"admin"})
	@Test
	void testCanEnterAdminUrlWithAdminAuthorities() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/listUsers"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(view().name("users/listReaders"));
    }
    @WithMockUser(username = "reader1", authorities = {"reader"})
	@Test
	void testCantEnterAdminUrlWithAuthenticated() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/listUsers"))
		.andExpect(MockMvcResultMatchers.status().is(403))
		.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
	@Test
	void testCantEnterAdminUrlWithNoAuthenticated() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/listUsers"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(redirectedUrl("http://localhost/login"));
    }
    @WithMockUser(username = "admin1", authorities = {"admin"})
	@Test
	void testCanEnterBooksVerifyWithAdminAuthorities() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/verify"))
		.andExpect(MockMvcResultMatchers.status().isOk());
    }
    @WithMockUser(username = "reader1", authorities = {"reader"})
	@Test
	void testCantEnterBooksVerifyWithAuthenticated() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/verify"))
		.andExpect(MockMvcResultMatchers.status().is(403))
		.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
	@Test
	void testCantEnterBooksVerifyWithNoAuthenticated() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/verify"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(redirectedUrl("http://localhost/login"));
    }
	@Test
	void testRedirectToLoginFromBooksNoAuthenticated() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/readBooks"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(redirectedUrl("http://localhost/login"));
	}
    @WithMockUser(username = "reader1", authorities = {"reader"})
	@Test
	void testCanEnterInUrlBooksWithAuthenticated() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/readBooks"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(view().name("books/booksList"));
    }
	@Test
	void testRedirectToLoginFromitBooksNoAuthenticated() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/itBooks/find"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(redirectedUrl("http://localhost/login"));
	}
    @WithMockUser(username = "reader1", authorities = {"reader"})
	@Test
	void testCanEnterInUrlitBooksWithAuthenticated() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/itBooks/find"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(view().name("itBooks/find"));
    }
	@Test
	void testRedirectToLoginFromMeetingsNoAuthenticated() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/meetings/find"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(redirectedUrl("http://localhost/login"));
	}
    @WithMockUser(username = "reader1", authorities = {"reader"})
	@Test
	void testCanEnterInUrlMeetingsWithAuthenticated() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/meetings/find"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(view().name("meetings/findMeetings"));
    }
	@Test
	void testRedirectToLoginFromPublicationsNoAuthenticated() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/2/publications"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(redirectedUrl("http://localhost/login"));
	}
    @WithMockUser(username = "reader1", authorities = {"reader"})
	@Test
	void testCanEnterInUrlPublicationsWithAuthenticated() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/2/publications"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(view().name("publications/publicationList"));
    }
	@Test
	void testRedirectToLoginFromReviewsNoAuthenticated() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/1/reviews"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(redirectedUrl("http://localhost/login"));
	}
    @WithMockUser(username = "reader1", authorities = {"reader"})
	@Test
	void testCanEnterInUrlReviewsWithAuthenticated() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/books/1/reviews"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(view().name("/reviews/reviewList"));
    }
	@Test
	void testCanEnter() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(redirectedUrl("/news"));
	}
	@Test
	void testCanEnterInNews() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/news"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(view().name("news/newList"));
	}
	@Test
	void testCanEnterInOups() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/oups"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(view().name("exception"));
	}

    
    
}
