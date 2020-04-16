package org.springframework.samples.petclinic.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.MeetingDashboard;
import org.springframework.samples.petclinic.service.MeetingDashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MeetingDashboardController {

	private MeetingDashboardService	 dashService;
    


    @Autowired
    public MeetingDashboardController(MeetingDashboardService dashService){
    		this.dashService = dashService;
    }
    
    @GetMapping("/admin/meetingDashboard")
	public String processDashboard(final Map<String, Object> model) {
    	MeetingDashboard meetingDashboard = this.dashService.getMeetingDashboard();
    	model.put("meetingDashboard",meetingDashboard);
		return "dashboards/dashboard";
		
	}
}
