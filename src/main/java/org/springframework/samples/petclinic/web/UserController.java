/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Reader;
import org.springframework.samples.petclinic.service.ReaderService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedUsernameException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class UserController {

	private static final String	VIEWS_READER_CREATE_FORM	= "users/createReaderForm";

	private static final String	VIEWS_READER_UPDATE_FORM	= "users/updateReaderForm";

	private final ReaderService	readerService;
	
	private static final String constant1= "notEmpty";
	
	private static final String constant2= "Must not be empty";


	@Autowired
	public UserController(final ReaderService readerService) {
		this.readerService = readerService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/users/new")
	public String initCreationForm(final Map<String, Object> model) {
		Reader reader = new Reader();
		model.put("reader", reader);
		return UserController.VIEWS_READER_CREATE_FORM;
	}

	@PostMapping(value = "/users/new")
	public String processCreationForm(@Valid final Reader reader, final BindingResult result) {
		boolean userEmpty = false;
		if (Strings.isBlank(reader.getUser().getUsername())) {
			userEmpty = true;
			result.rejectValue("user.username", constant1, constant2);
		}
		if (Strings.isBlank(reader.getUser().getPassword())) {
			userEmpty = true;
			result.rejectValue("user.password", constant1, constant2);
		}
		if (result.hasErrors() || userEmpty) {
			return UserController.VIEWS_READER_CREATE_FORM;
		} else {
			//creating reader, user, and authority
			try {
				this.readerService.saveReader(reader);
			} catch (DuplicatedUsernameException e) {
				result.rejectValue("user.username", "duplicate", "Already exists");
				return UserController.VIEWS_READER_CREATE_FORM;
			}
			return "redirect:/";
		}
	}

	@GetMapping(value = "/users/update")
	public String initUpdateForm(final Map<String, Object> model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userdetails = (UserDetails) auth.getPrincipal();
		Reader reader = this.readerService.findReaderByUsername(userdetails.getUsername());
		model.put("reader", reader);
		return UserController.VIEWS_READER_UPDATE_FORM;
	}

	@PostMapping(value = "/users/update")
	public String processUpdateForm(@Valid final Reader reader, final BindingResult result) throws DuplicatedUsernameException {
		boolean userEmpty = false;
		if (Strings.isBlank(reader.getUser().getPassword())) {
			userEmpty = true;
			result.rejectValue("user.password", constant1, constant2);
		}
		if (result.hasErrors() || userEmpty) {
			return UserController.VIEWS_READER_UPDATE_FORM;
		} else {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userdetails = (UserDetails) auth.getPrincipal();
			Reader reader0 = this.readerService.findReaderByUsername(userdetails.getUsername());
			reader0.setAddress(reader.getAddress());
			reader0.setCity(reader.getCity());
			reader0.setFirstName(reader.getFirstName());
			reader0.setLastName(reader.getLastName());
			reader0.setTelephone(reader.getTelephone());
			reader0.getUser().setPassword(reader.getUser().getPassword());
			this.readerService.saveReader(reader0);
		}
		return "redirect:/";
	}

}
