package org.springframework.samples.petclinic.model;

import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.web.PublicationValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

class PublicationValidatorTests {
	private PublicationValidator createValidator() {
		return new PublicationValidator();
	}
	
	@Test
	void shouldNoValidateTitleEmpty() {
		Publication publication = this.create();
		publication.setTitle(" ");
	
		
		PublicationValidator publicationValidator = createValidator();
		Errors errors = new BeanPropertyBindingResult(publication, "publication");
		publicationValidator.validate(publication, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.hasFieldErrors("title")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");   
	}
	
	@Test
	void shouldNoValidateDescriptionEmpty() {
		Publication publication = this.create();
		publication.setDescription(" ");

		
		PublicationValidator publicationValidator = createValidator();
		Errors errors = new BeanPropertyBindingResult(publication, "publication");
		publicationValidator.validate(publication, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.hasFieldErrors("description")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");   
	}
	
	@Test
	void shouldNoValidateImageURLEmpty() {
		Publication publication = this.create();
		publication.setImage(" ");
		
		PublicationValidator publicationValidator = createValidator();
		Errors errors = new BeanPropertyBindingResult(publication, "publication");
		publicationValidator.validate(publication, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.hasFieldErrors("image")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");   
	}
	
	@Test
	void shouldNoValidateImageURL() {
		Publication publication = this.create();
		publication.setImage("//pictures.abebooks.com/isbn/9780575081406-es.jpg");
		
		PublicationValidator publicationValidator = createValidator();
		Errors errors = new BeanPropertyBindingResult(publication, "publication");
		publicationValidator.validate(publication, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.hasFieldErrors("image")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Enter a valid URL");   
	}
	
	private Publication create() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Publication publication = new Publication();
		publication.setTitle("Titulo");
		publication.setDescription("Esto es una prueba");
		publication.setImage("https://pictures.abebooks.com/isbn/9780575081406-es.jpg");
		
		return publication;
	}

}
