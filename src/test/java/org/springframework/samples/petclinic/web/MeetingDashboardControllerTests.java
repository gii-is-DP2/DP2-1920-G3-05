package org.springframework.samples.petclinic.web;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.service.MeetingDashboardService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = MeetingDashboardController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
 class MeetingDashboardControllerTests {

	@MockBean
	private MeetingDashboardService meetService;

	@Autowired
	private MockMvc					mockMvc;
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessDashboard() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/meetingDashboard")).andExpect(status().isOk())
		.andExpect(view().name("dashboards/dashboard"));
	}
}
