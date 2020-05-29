
package org.springframework.samples.petclinic.web;

import java.net.URL;

import org.apache.logging.log4j.util.Strings;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class BookValidator implements Validator {

	private static final String CONSTANT1= "Must not be empty";

	@Override
	public void validate(final Object target, final Errors errors) {
		Book book = (Book) target;
		String ISBN = book.getISBN();

		if (!this.validateISBN(ISBN)) {
			errors.rejectValue("ISBN", "Enter a valid ISBN", "Enter a valid ISBN");

		}

		if (Strings.isBlank(book.getTitle())) {
			errors.rejectValue("title", CONSTANT1, CONSTANT1);
		}

		if (Strings.isBlank(book.getAuthor())) {
			errors.rejectValue("author", CONSTANT1, CONSTANT1);
		}
		if (Strings.isBlank(book.getEditorial())) {
			errors.rejectValue("editorial", CONSTANT1, CONSTANT1);
		}
		if (book.getGenre() == null) {
			errors.rejectValue("genre", CONSTANT1, CONSTANT1);
		}
		if (book.getPages() == null) {
			errors.rejectValue("pages", CONSTANT1, CONSTANT1);
		}else if(book.getPages() < 1) {
			errors.rejectValue("pages", "Must be > 1", "Must be > 1");
		}
		if (Strings.isBlank(book.getSynopsis())) {
			errors.rejectValue("synopsis", CONSTANT1, CONSTANT1);
		}
		if (book.getPublicationDate() == null) {
			errors.rejectValue("publicationDate", CONSTANT1, CONSTANT1);

		}
		if (Strings.isBlank(book.getImage())) {
			errors.rejectValue("image", CONSTANT1, CONSTANT1);

		}else if (this.isValid(book.getImage()) == false) {
			errors.rejectValue("image", "Enter a valid URL", "Enter a valid URL");
		}

	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return Book.class.isAssignableFrom(clazz);
	}

	public boolean validateISBN(String isbn) {
		if (isbn == null) {
			return false;
		}
		//remove any hyphens
		isbn = isbn.replaceAll("-", "");

		//must be a 13 digit ISBN
		if (isbn.length() != 13) {
			return false;
		}
		try {
			int tot = 0;
			for (int i = 0; i < 12; i++) {
				int digit = Integer.parseInt(isbn.substring(i, i + 1));
				tot += i % 2 == 0 ? digit * 1 : digit * 3;
			}
			//checksum must be 0-9. If calculated as 10 then = 0
			int checksum = 10 - tot % 10;
			if (checksum == 10) {
				checksum = 0;
			}
			return checksum == Integer.parseInt(isbn.substring(12));
		} catch (NumberFormatException nfe) {
			//to catch invalid ISBNs that have non-numeric characters in them
			return false;
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
