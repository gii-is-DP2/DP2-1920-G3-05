package org.springframework.samples.petclinic.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalDateTimeFormatterTests {

    private LocalDateTimeFormatter sut;

    @BeforeEach
    void setup(){
        sut = new LocalDateTimeFormatter();
    }

    @Test
    void testPrint(){
        LocalDateTime ldt = LocalDateTime.of(2020, 03, 21, 12, 41);
        String dateTime = sut.print(ldt, Locale.ENGLISH);
        assertEquals("2020-03-21T12:41", dateTime);
    }

    @Test
    void shouldParse() throws ParseException {
        String ldt = "2020-03-21T12:41";
        LocalDateTime parsedLdt = sut.parse(ldt, Locale.ENGLISH);
        assertEquals(ldt, sut.print(parsedLdt, Locale.ENGLISH));
    }

    @Test
    void shouldThrowPareException() throws ParseException {
        String ldt = "2020-12-2112:41";
        Assertions.assertThrows(ParseException.class, ()->sut.parse(ldt, Locale.ENGLISH));
    }
}