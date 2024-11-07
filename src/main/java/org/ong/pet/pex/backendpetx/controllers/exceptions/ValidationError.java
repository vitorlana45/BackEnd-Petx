package org.ong.pet.pex.backendpetx.controllers.exceptions;

import lombok.Getter;

import java.util.*;

@Getter
public class ValidationError extends StandardError {
	private static final long serialVersionUID = 1L;

	private List<FieldMessage> errors = new ArrayList<>();

	public void addError(String fieldName, String message) {
		errors.add(new FieldMessage(fieldName, message));
	}
}
