package org.springframework.samples.petclinic.web;

import org.apache.logging.log4j.util.Strings;
import org.springframework.samples.petclinic.model.Review;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ReviewValidator implements Validator{


	@Override
	public boolean supports(Class<?> clazz) {
		return Review.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Review review = (Review) target;
		
		if(Strings.isBlank(review.getTitle())) {
			errors.rejectValue("title", "Must not be empty", "Must not be empty");
		}
		
		if(review.getRaiting() == null) {
			errors.rejectValue("raiting", "Must not be empty", "Must not be empty");
		}else if(review.getRaiting() < 0 || review.getRaiting()>5){
			errors.rejectValue("raiting", "Must be an integer from 0 to 5", "Must be an integer from 0 to 5");
		}
		
		if(Strings.isBlank(review.getOpinion())) {
			errors.rejectValue("opinion", "Must not be empty", "Must not be empty");
		}
	}

	
}
