package org.springframework.samples.petclinic.model;

import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.web.ReviewValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class ReviewValidatorTest {

    private ReviewValidator createValidator() {
        return new ReviewValidator();
    }

    @Test
    void shouldNoValidateTitleEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Review review = new Review();
        review.setTitle("");
        review.setRaiting(3);
        review.setOpinion("Opinion");

        ReviewValidator reviewValidator = createValidator();
        Errors errors = new BeanPropertyBindingResult(review, "review");
        reviewValidator.validate(review, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("title")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");   
    }

    @Test
    void shouldNoValidateRatingEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Review review = new Review();
        review.setTitle("Title");
        review.setOpinion("Opinion");

        ReviewValidator reviewValidator = createValidator();
        Errors errors = new BeanPropertyBindingResult(review, "review");
        reviewValidator.validate(review, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("raiting")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");
    }

    @Test
    void shouldNoValidateRatingNotInRange() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Review review = new Review();
        review.setTitle("Title");
        review.setRaiting(6);
        review.setOpinion("Opinion");

        ReviewValidator reviewValidator = createValidator();
        Errors errors = new BeanPropertyBindingResult(review, "review");
        reviewValidator.validate(review, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("raiting")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must be an integer from 0 to 5");
    }

    @Test
    void shouldNoValidateOpinionEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Review review = new Review();
        review.setTitle("Title");
        review.setRaiting(3);
        review.setOpinion("");

        ReviewValidator reviewValidator = createValidator();
        Errors errors = new BeanPropertyBindingResult(review, "review");
        reviewValidator.validate(review, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("opinion")).isTrue();
        Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");
     }

     @Test
     void shouldValidate() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Review review = new Review();
        review.setTitle("Title");
        review.setRaiting(3);
        review.setOpinion("Opinion");

        ReviewValidator reviewValidator = createValidator();
        Errors errors = new BeanPropertyBindingResult(review, "review");
        reviewValidator.validate(review, errors);
        Assertions.assertThat(errors.getAllErrors()).isEmpty();

     }
}
