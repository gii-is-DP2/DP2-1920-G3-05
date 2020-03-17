package org.springframework.samples.petclinic.service.exceptions;

public class ReadOrWishedBookException extends Exception{

	public ReadOrWishedBookException(String errorMessage) {
		super(errorMessage);
	}

}
