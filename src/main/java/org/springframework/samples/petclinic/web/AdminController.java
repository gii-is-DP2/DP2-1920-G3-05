package org.springframework.samples.petclinic.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Reader;
import org.springframework.samples.petclinic.service.ReaderService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminController {

	@Autowired
	private ReaderService			readerService;
	
	@GetMapping("/admin/listUsers")
	public String processFindForm(Reader reader, final BindingResult result, final Map<String, Object> model) {

		Iterable<Reader> results = this.readerService.findReaders();
			model.put("selections", results);
			return "users/listReaders";
	}
	
	@GetMapping("/admin/users/{userId}/verify")
	public String verifyUser(@PathVariable("userId") final int userId) {
	    	this.readerService.verifyUser(userId);
			return "redirect:/admin/listUsers";
		
	}
}
