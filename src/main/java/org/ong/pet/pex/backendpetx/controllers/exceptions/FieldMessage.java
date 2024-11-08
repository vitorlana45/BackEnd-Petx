package org.ong.pet.pex.backendpetx.controllers.exceptions;

import lombok.*;

import java.io.*;

@Getter
@Setter
@NoArgsConstructor
public class FieldMessage implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private String fieldName;
	private String message;

	public FieldMessage(String fieldName, String message) {
		super();
		this.fieldName = fieldName;
		this.message = message;
	}
}
