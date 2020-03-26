package org.springframework.samples.petclinic.model;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Locale;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.web.MeetingValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class MeetingValidatorTests {
    
    private MeetingValidator createValidator() {
		return new MeetingValidator();
    }

    @BeforeEach
    void setup(){
        Instant.now(Clock.fixed(Instant.parse("2020-03-21T16:00:00Z"), ZoneOffset.UTC)); 
        //Fijamos una fecha y hora para que las pruebas no dependan del momento en el que se ejecuten
    }

    @Test
    void shouldNoValidateNameEmpty(){
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Meeting meeting = new Meeting();
        LocalDateTime begin = LocalDateTime.of(2020, 10, 10, 17, 00, 00);
        LocalDateTime end = LocalDateTime.of(2020, 10, 10, 19, 00, 00);
        meeting.setName("");
        meeting.setPlace("Library");
        meeting.setStart(begin);
        meeting.setEnd(end);
        meeting.setCapacity(50);

        MeetingValidator validator = createValidator();
        Errors errors = new BeanPropertyBindingResult(meeting, "meeting");
        validator.validate(meeting, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("name")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");
    }

    @Test
    void shouldNoValidateNameLess3Characters(){
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Meeting meeting = new Meeting();
        LocalDateTime begin = LocalDateTime.of(2020, 10, 10, 17, 00, 00);
        LocalDateTime end = LocalDateTime.of(2020, 10, 10, 19, 00, 00);
        meeting.setName("A");
        meeting.setPlace("Library");
        meeting.setStart(begin);
        meeting.setEnd(end);
        meeting.setCapacity(50);

        MeetingValidator validator = createValidator();
        Errors errors = new BeanPropertyBindingResult(meeting, "meeting");
        validator.validate(meeting, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("name")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must have at least 3 characters");
    }

    @Test
    void shouldNoValidatePlaceEmpty(){
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Meeting meeting = new Meeting();
        LocalDateTime begin = LocalDateTime.of(2020, 10, 10, 17, 00, 00);
        LocalDateTime end = LocalDateTime.of(2020, 10, 10, 19, 00, 00);
        meeting.setName("Meeting");
        meeting.setPlace("");
        meeting.setStart(begin);
        meeting.setEnd(end);
        meeting.setCapacity(50);

        MeetingValidator validator = createValidator();
        Errors errors = new BeanPropertyBindingResult(meeting, "meeting");
        validator.validate(meeting, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("place")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");
    }

    @Test
    void shouldNoValidateCapacityNull(){
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Meeting meeting = new Meeting();
        LocalDateTime begin = LocalDateTime.of(2020, 10, 10, 17, 00, 00);
        LocalDateTime end = LocalDateTime.of(2020, 10, 10, 19, 00, 00);
        meeting.setName("Meeting");
        meeting.setPlace("Library");
        meeting.setStart(begin);
        meeting.setEnd(end);

        MeetingValidator validator = createValidator();
        Errors errors = new BeanPropertyBindingResult(meeting, "meeting");
        validator.validate(meeting, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("capacity")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");
    }

    @Test
    void shouldNoValidateCapacityLessThan5(){
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Meeting meeting = new Meeting();
        LocalDateTime begin = LocalDateTime.of(2020, 10, 10, 17, 00, 00);
        LocalDateTime end = LocalDateTime.of(2020, 10, 10, 19, 00, 00);
        meeting.setName("Meeting");
        meeting.setPlace("Library");
        meeting.setStart(begin);
        meeting.setEnd(end);
        meeting.setCapacity(2);

        MeetingValidator validator = createValidator();
        Errors errors = new BeanPropertyBindingResult(meeting, "meeting");
        validator.validate(meeting, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("capacity")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must be at least 5");
    }

    @Test
    void shouldNoValidateStartNull(){
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Meeting meeting = new Meeting();
        LocalDateTime end = LocalDateTime.of(2020, 10, 10, 19, 00, 00);
        meeting.setName("Meeting");
        meeting.setPlace("Library");
        meeting.setEnd(end);
        meeting.setCapacity(5);

        MeetingValidator validator = createValidator();
        Errors errors = new BeanPropertyBindingResult(meeting, "meeting");
        validator.validate(meeting, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("start")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");
    }

    @Test
    void shouldNoValidateEndNull(){
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Meeting meeting = new Meeting();
        LocalDateTime begin = LocalDateTime.of(2020, 10, 10, 17, 00, 00);
        meeting.setName("Meeting");
        meeting.setPlace("Library");
        meeting.setStart(begin);
        meeting.setCapacity(5);

        MeetingValidator validator = createValidator();
        Errors errors = new BeanPropertyBindingResult(meeting, "meeting");
        validator.validate(meeting, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("end")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");
    }

    @Test
    void shouldNoValidateEndBeforeStart(){
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Meeting meeting = new Meeting();
        LocalDateTime end = LocalDateTime.of(2020, 10, 10, 17, 00, 00);
        LocalDateTime begin = LocalDateTime.of(2020, 10, 10, 19, 00, 00);
        meeting.setName("Meeting");
        meeting.setPlace("Library");
        meeting.setStart(begin);
        meeting.setEnd(end);
        meeting.setCapacity(5);

        MeetingValidator validator = createValidator();
        Errors errors = new BeanPropertyBindingResult(meeting, "meeting");
        validator.validate(meeting, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(2); //tb salta la de menos de una hora
        Assertions.assertThat(errors.hasFieldErrors("end")).isTrue();
        Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("End date must be after start date");
    }

    @Test
    void shouldNoValidate3DaysAdvanced(){
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Meeting meeting = new Meeting();
        LocalDateTime begin = LocalDateTime.of(2020, 03, 04, 17, 00, 00);
        LocalDateTime end = LocalDateTime.of(2020, 03, 04, 19, 00, 00);
        meeting.setName("Meeting");
        meeting.setPlace("Library");
        meeting.setStart(begin);
        meeting.setEnd(end);
        meeting.setCapacity(5);

        MeetingValidator validator = createValidator();
        Errors errors = new BeanPropertyBindingResult(meeting, "meeting");
        validator.validate(meeting, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("start")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Meeting must be planned at least 3 days in advanced");
    }

    @Test
    void shouldNoValidate1HourDuration(){
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Meeting meeting = new Meeting();
        LocalDateTime begin = LocalDateTime.of(2020, 10, 10, 17, 00, 00);
        LocalDateTime end = LocalDateTime.of(2020, 10, 10, 17, 30, 00);
        meeting.setName("Meeting");
        meeting.setPlace("Library");
        meeting.setStart(begin);
        meeting.setEnd(end);
        meeting.setCapacity(5);

        MeetingValidator validator = createValidator();
        Errors errors = new BeanPropertyBindingResult(meeting, "meeting");
        validator.validate(meeting, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("end")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Meeting must last at least 1 hour");
    }

    @Test
    void shouldValidate(){
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Meeting meeting = new Meeting();
        LocalDateTime begin = LocalDateTime.of(2020, 10, 10, 17, 00, 00);
        LocalDateTime end = LocalDateTime.of(2020, 10, 10, 19, 00, 00);
        meeting.setName("Meeting");
        meeting.setPlace("Library");
        meeting.setStart(begin);
        meeting.setEnd(end);
        meeting.setCapacity(50);

        MeetingValidator validator = createValidator();
        Errors errors = new BeanPropertyBindingResult(meeting, "meeting");
        validator.validate(meeting, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(0);

    }
    
}