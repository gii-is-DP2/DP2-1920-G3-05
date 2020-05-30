package org.springframework.samples.petclinic.web.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
  webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
 class AdminControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(username = "admin1", authorities = {"admin"})
	@Test
	void testShowReadersList() throws Exception{
        this.mockMvc.perform(get("/admin/listUsers")).andExpect(status().isOk())
                    .andExpect(model().attributeExists("selections"))
	            	.andExpect(view().name("users/listReaders"));
	}
	
    @WithMockUser(username = "admin1", authorities = {"admin"})
	@Test
	void testverifyUser() throws Exception{
        this.mockMvc.perform(get("/admin/users/{userId}/verify",3).with(csrf()))
                    .andExpect(status().is3xxRedirection())
	                .andExpect(redirectedUrl("/admin/listUsers"));
    }

    @WithMockUser(username = "reader1", authorities = {"reader"})
	@Test
	void testverifyUserImNotAdmin() throws Exception{
        this.mockMvc.perform(get("/admin/users/{userId}/verify",3).with(csrf()))
                    .andExpect(status().is4xxClientError());
    }
    
}