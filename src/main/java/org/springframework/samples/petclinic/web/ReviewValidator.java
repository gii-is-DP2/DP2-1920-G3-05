package org.springframework.samples.petclinic.web;

import org.apache.logging.log4j.util.Strings;
import org.springframework.samples.petclinic.model.Review;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ReviewValidator implements Validator{

	private static final String CONSTANT1= "Must not be empty";

	@Override
	public boolean supports(Class<?> clazz) {
		return Review.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Review review = (Review) target;
		
		if(Strings.isBlank(review.getTitle())) {
			errors.rejectValue("title", CONSTANT1, CONSTANT1);
		}
		
		if(review.getRaiting() == null) {
			errors.rejectValue("raiting", CONSTANT1, CONSTANT1);
		}else if(review.getRaiting() < 0 || review.getRaiting()>5){
			errors.rejectValue("raiting", "Must be an integer from 0 to 5", "Must be an integer from 0 to 5");
		}
		
		if(Strings.isBlank(review.getOpinion())) {
			errors.rejectValue("opinion", CONSTANT1, CONSTANT1);
		}
	}

	
}
