package org.ong.pet.pex.backendpetx.service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.ong.pet.pex.backendpetx.controllers.exceptions.FieldMessage;
import org.ong.pet.pex.backendpetx.dto.request.UsuarioDTO;
import org.ong.pet.pex.backendpetx.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class ValidadorDeInsercaoDeUsuario implements ConstraintValidator<InserirUsuarioValido, UsuarioDTO> {

	@Autowired
	private UsuarioRepository repository;

	@Override
	public void initialize(InserirUsuarioValido ann) {
	}

	@Override
	public boolean isValid(UsuarioDTO dto, ConstraintValidatorContext context) {

		List<FieldMessage> list = new ArrayList<>();

		UserDetails user = repository.findByEmail(dto.email());
		if (user != null) {
			list.add(new FieldMessage("email", "Email já existe"));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
