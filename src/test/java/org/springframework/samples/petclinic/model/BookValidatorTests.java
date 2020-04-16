package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.web.BookValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

class BookValidatorTests {
	private BookValidator createValidator() {
		return new BookValidator();
	}

	@Test
	void shouldNoValidateTitleEmpty() {
		Book book = this.create();
		book.setTitle(" ");
		BookValidator bookValidator = createValidator();
		Errors errors = new BeanPropertyBindingResult(book, "book");
		bookValidator.validate(book, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.hasFieldErrors("title")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");   
	}

	@Test
	void shouldNoValidateAuthorEmpty() {
		Book book = this.create();
		book.setAuthor(" ");
		BookValidator bookValidator = createValidator();
		Errors errors = new BeanPropertyBindingResult(book, "book");
		bookValidator.validate(book, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.hasFieldErrors("author")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");   
	}

	@Test
	void shouldNoValidateEditorialEmpty() {
		Book book = this.create();
		book.setEditorial(" ");
		BookValidator bookValidator = createValidator();
		Errors errors = new BeanPropertyBindingResult(book, "book");
		bookValidator.validate(book, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.hasFieldErrors("editorial")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");   
	}

	@Test
	void shouldNoValidateGenreEmpty() {
		Book book = this.create();
		book.setGenre(null);
		BookValidator bookValidator = createValidator();
		Errors errors = new BeanPropertyBindingResult(book, "book");
		bookValidator.validate(book, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.hasFieldErrors("genre")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");   
	}

	@Test
	void shouldNoValidateISBNEmpty() {
		Book book = this.create();
		book.setISBN("0123456789");
		BookValidator bookValidator = createValidator();
		Errors errors = new BeanPropertyBindingResult(book, "book");
		bookValidator.validate(book, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.hasFieldErrors("ISBN")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Enter a valid ISBN");   
	}

	@Test
	void shouldNoValidateImageURL() {
		Book book = this.create();
		book.setImage("pictures.abebooks.com/isbn/9780575081406-es.jpg");
		
		BookValidator bookValidator = createValidator();
		Errors errors = new BeanPropertyBindingResult(book, "book");
		bookValidator.validate(book, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.hasFieldErrors("image")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Enter a valid URL");   
	}
	@Test
	void shouldNoValidateImageEmpty() {
		Book book = this.create();
		book.setImage("");
		
		BookValidator bookValidator = createValidator();
		Errors errors = new BeanPropertyBindingResult(book, "book");
		bookValidator.validate(book, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.hasFieldErrors("image")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");   
	}

	@Test
	void shouldNoValidatePageEmpty() {
		Book book = this.create();
		book.setPages(null);
		
		BookValidator bookValidator = createValidator();
		Errors errors = new BeanPropertyBindingResult(book, "book");
		bookValidator.validate(book, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.hasFieldErrors("pages")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");   
	}

	@Test
	void shouldNoValidatePageLess1() {
		Book book = this.create();
		book.setPages(-1);
	
		BookValidator bookValidator = createValidator();
		Errors errors = new BeanPropertyBindingResult(book, "book");
		bookValidator.validate(book, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.hasFieldErrors("pages")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must be > 1");   
	}

	@Test
	void shouldNoValidatePublicationDateEmpty() {
		Book book = this.create();
		book.setPublicationDate(null);

		BookValidator bookValidator = createValidator();
		Errors errors = new BeanPropertyBindingResult(book, "book");
		bookValidator.validate(book, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.hasFieldErrors("publicationDate")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");   
	}


	@Test
	void shouldNoValidateSynopsisEmpty() {
		Book book = this.create();
		book.setSynopsis("");

		BookValidator bookValidator = createValidator();
		Errors errors = new BeanPropertyBindingResult(book, "book");
		bookValidator.validate(book, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.hasFieldErrors("synopsis")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");   
	}
	
	private Book create() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Book book = new Book();
		book.setTitle("Prueba");	
		book.setAuthor("author");
		book.setEditorial("editorial");
		Genre genre = new Genre();
		genre.setName("name");
		book.setGenre(new Genre());
		book.setISBN("9780345805362");
		book.setImage("https://pictures.abebooks.com/isbn/9780575081406-es.jpg");
		book.setPages(100);
		book.setPublicationDate(LocalDate.now());
		book.setSynopsis("He robado princesas a reyes agónicos. Incendié la ciudad de Trebon. He pasado la noche con Felurian y he despertado vivo y cuerdo. Me expulsaron de la Universidad a una edad a la que a la mayoría todavía no los dejan entrar. He recorrido de noche caminos de los que otros no se atreven a hablar ni siquiera de día. He hablado con dioses, he amado a mujeres y he escrito canciones que hacen llorar a los bardos. Me llamo Kvothe. Quizá hayas oído hablar de mí");
		
		return book;
		
	}

}
