package org.springframework.samples.petclinic.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Genre;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.samples.petclinic.service.PetService;

@ExtendWith(MockitoExtension.class)
class GenreFormatterTests {
	
	@Mock
	private BookService bookService;

	private GenreFormatter genreFormatter;

	@BeforeEach
	void setup() {
		genreFormatter = new GenreFormatter(bookService);
	}

	@Test
	void testPrint() {
		Genre genre = new Genre();
		genre.setName("Novela");
		String genreName = genreFormatter.print(genre, Locale.ENGLISH);
		assertEquals("Novela", genreName);
	}

	@Test
	void shouldParse() throws ParseException {
		Mockito.when(bookService.findGenre()).thenReturn(makeGenres());
		Genre genre = genreFormatter.parse("Amor", Locale.ENGLISH);
		assertEquals("Amor", genre.getName());
	}

	@Test
	void shouldThrowParseException() throws ParseException {
		Mockito.when(bookService.findGenre()).thenReturn(makeGenres());
		Assertions.assertThrows(ParseException.class, () -> {
			genreFormatter.parse("Policia", Locale.ENGLISH);
		});
	}

	/**
	 * Helper method to produce some sample pet types just for test purpose
	 * @return {@link Collection} of {@link PetType}
	 */
	private Collection<Genre> makeGenres() {
		Collection<Genre> genres = new ArrayList<>();
		genres.add(new Genre() {
			{
				setName("Misterio");
			}
		});
		genres.add(new Genre() {
			{
				setName("Amor");
			}
		});
		return genres;
	}

}
