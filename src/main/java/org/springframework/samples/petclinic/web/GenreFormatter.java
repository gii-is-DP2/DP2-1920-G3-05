
package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.Genre;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.stereotype.Component;

@Component
public class GenreFormatter implements Formatter<Genre> {

	private final BookService boService;


	@Autowired
	public GenreFormatter(final BookService bookService) {
		this.boService = bookService;
	}

	@Override
	public String print(final Genre genre, final Locale locale) {
		return genre.getName();
	}

	@Override
	public Genre parse(final String text, final Locale locale) throws ParseException {
		Collection<Genre> findGenre = this.boService.findGenre();
		for (Genre genre : findGenre) {
			if (genre.getName().equals(text)) {
				return genre;
			}
		}
		throw new ParseException("type not found: " + text, 0);
	}

}
