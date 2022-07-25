package vn.trinhtung.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import vn.trinhtung.dto.UserDto;
import vn.trinhtung.repository.UserRepository;

@Component
public class RegisterUserValidator implements Validator {
	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(UserDto.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserDto user = (UserDto) target;
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			errors.rejectValue("email", null, "Email đã tồn tại");
		}
	}

}
