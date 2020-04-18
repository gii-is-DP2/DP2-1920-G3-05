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
        Review review = this.create();
        review.setTitle("");


        ReviewValidator reviewValidator = createValidator();
        Errors errors = new BeanPropertyBindingResult(review, "review");
        reviewValidator.validate(review, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("title")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");   
    }

    @Test
    void shouldNoValidateRatingEmpty() {
        Review review = this.create();
        review.setRaiting(null);

        ReviewValidator reviewValidator = createValidator();
        Errors errors = new BeanPropertyBindingResult(review, "review");
        reviewValidator.validate(review, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("raiting")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must not be empty");
    }

    @Test
    void shouldNoValidateRatingNotInRange() {
        Review review = this.create();
        review.setRaiting(6);

        ReviewValidator reviewValidator = createValidator();
        Errors errors = new BeanPropertyBindingResult(review, "review");
        reviewValidator.validate(review, errors);
        Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
        Assertions.assertThat(errors.hasFieldErrors("raiting")).isTrue();
		Assertions.assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("Must be an integer from 0 to 5");
    }

    @Test
    void shouldNoValidateOpinionEmpty() {
        Review review = this.create();
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
        Review review = this.create();

        ReviewValidator reviewValidator = createValidator();
        Errors errors = new BeanPropertyBindingResult(review, "review");
        reviewValidator.validate(review, errors);
        Assertions.assertThat(errors.getAllErrors()).isEmpty();

     }
     
     private Review create() {
    	 LocaleContextHolder.setLocale(Locale.ENGLISH);
         Review review = new Review();
         review.setTitle("Title");
         review.setRaiting(3);
         review.setOpinion("Opinion");
         
         return review;
     }
}
