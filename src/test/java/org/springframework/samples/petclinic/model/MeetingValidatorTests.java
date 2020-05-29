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

 class MeetingValidatorTests {
    
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
        Meeting meeting = this.create();
        meeting.setName("");

        MeetingValidator validator = createValidator();
        Errors errors = new BeanPropertyBindingResult(meeting, "meeting");
        validator.validate(meeting, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("name")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");
    }

    @Test
    void shouldNoValidateNameLess3Characters(){
    	Meeting meeting = this.create();
        meeting.setName("A");

        MeetingValidator validator = createValidator();
        Errors errors = new BeanPropertyBindingResult(meeting, "meeting");
        validator.validate(meeting, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("name")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must have at least 3 characters");
    }

    @Test
    void shouldNoValidatePlaceEmpty(){
        Meeting meeting = this.create();
        meeting.setPlace("");


        MeetingValidator validator = createValidator();
        Errors errors = new BeanPropertyBindingResult(meeting, "meeting");
        validator.validate(meeting, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("place")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");
    }

    @Test
    void shouldNoValidateCapacityNull(){
        Meeting meeting = this.create();
        meeting.setCapacity(null);

        MeetingValidator validator = createValidator();
        Errors errors = new BeanPropertyBindingResult(meeting, "meeting");
        validator.validate(meeting, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("capacity")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");
    }

    @Test
    void shouldNoValidateCapacityLessThan5(){
        Meeting meeting = this.create();
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
        Meeting meeting = this.create();
        meeting.setStart(null);

        MeetingValidator validator = createValidator();
        Errors errors = new BeanPropertyBindingResult(meeting, "meeting");
        validator.validate(meeting, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("start")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");
    }

    @Test
    void shouldNoValidateEndNull(){
    	Meeting meeting = this.create();
    	meeting.setEnd(null);

        MeetingValidator validator = createValidator();
        Errors errors = new BeanPropertyBindingResult(meeting, "meeting");
        validator.validate(meeting, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("end")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");
    }

    @Test
    void shouldNoValidateEndBeforeStart(){
        Meeting meeting = this.create();
        LocalDateTime end = LocalDateTime.of(2020, 10, 10, 17, 00, 00);
        LocalDateTime begin = LocalDateTime.of(2020, 10, 10, 19, 00, 00);
       
        meeting.setStart(begin);
        meeting.setEnd(end);

        MeetingValidator validator = createValidator();
        Errors errors = new BeanPropertyBindingResult(meeting, "meeting");
        validator.validate(meeting, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(2); //tb salta la de menos de una hora
        Assertions.assertThat(errors.hasFieldErrors("end")).isTrue();
        Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("End date must be after start date");
    }

    @Test
    void shouldNoValidate3DaysAdvanced(){
        Meeting meeting = this.create();
        LocalDateTime begin = LocalDateTime.of(2020, 03, 04, 17, 00, 00);
        LocalDateTime end = LocalDateTime.of(2020, 03, 04, 19, 00, 00);
        meeting.setStart(begin);
        meeting.setEnd(end);


        MeetingValidator validator = createValidator();
        Errors errors = new BeanPropertyBindingResult(meeting, "meeting");
        validator.validate(meeting, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("start")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Meeting must be planned at least 3 days in advanced");
    }

    @Test
    void shouldNoValidate1HourDuration(){
        Meeting meeting = this.create();
        LocalDateTime begin = LocalDateTime.of(2020, 10, 10, 17, 00, 00);
        LocalDateTime end = LocalDateTime.of(2020, 10, 10, 17, 30, 00);

        meeting.setStart(begin);
        meeting.setEnd(end);


        MeetingValidator validator = createValidator();
        Errors errors = new BeanPropertyBindingResult(meeting, "meeting");
        validator.validate(meeting, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("end")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Meeting must last at least 1 hour");
    }

    @Test
    void shouldValidate(){
        Meeting meeting = this.create();
        MeetingValidator validator = createValidator();
        Errors errors = new BeanPropertyBindingResult(meeting, "meeting");
        validator.validate(meeting, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(0);

    }
    private Meeting create() {
    	LocaleContextHolder.setLocale(Locale.ENGLISH);
        Meeting meeting = new Meeting();
        LocalDateTime begin = LocalDateTime.of(2020, 10, 10, 17, 00, 00);
        LocalDateTime end = LocalDateTime.of(2020, 10, 10, 19, 00, 00);
        meeting.setName("Meeting");
        meeting.setPlace("Library");
        meeting.setStart(begin);
        meeting.setEnd(end);
        meeting.setCapacity(50);
        return meeting;
    	
    }
    
}