package org.springframework.samples.petclinic.web;

import java.net.URL;

import org.apache.logging.log4j.util.Strings;
import org.springframework.samples.petclinic.model.Publication;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PublicationValidator implements Validator{


	
	@Override
	public boolean supports(Class<?> clazz) {
		return Publication.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Publication publication = (Publication) target;
		if (Strings.isBlank(publication.getTitle())) {
			errors.rejectValue("title", "Must not be empty", "Must not be empty");
		}

		if (Strings.isBlank(publication.getDescription())) {
			errors.rejectValue("description", "Must not be empty", "Must not be empty");
		}
		if (Strings.isBlank(publication.getImage())) {
			errors.rejectValue("image", "Must not be empty", "Must not be empty");

		}else if (this.isValid(publication.getImage()) == false) {
			errors.rejectValue("image", "Enter a valid URL", "Enter a valid URL");
		}
		
	}
	
	public boolean isValid(final String url) {
		/* Try creating a valid URL */
		try {
			new URL(url).toURI();
			return true;
		}

		// If there was an Exception
		// while creating URL object
		catch (Exception e) {
			return false;
		}
	}


}
