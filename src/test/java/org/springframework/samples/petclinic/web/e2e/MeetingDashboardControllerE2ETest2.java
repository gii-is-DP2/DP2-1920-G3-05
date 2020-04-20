package org.springframework.samples.petclinic.web.e2e;



import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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


@ExtendWith(SpringExtension.class)
@SpringBootTest(
  webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class MeetingDashboardControllerE2ETest2 {
	


	@Autowired
	private MockMvc mockMvc;	

	@WithMockUser(username="admin1",authorities= {"admin"})
	@Test
	void testProcessDashboard() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/meetingDashboard")).andExpect(status().isOk())
		.andExpect(view().name("dashboards/dashboard"));
	}


}
