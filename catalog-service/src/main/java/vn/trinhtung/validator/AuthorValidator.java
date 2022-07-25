package vn.trinhtung.validator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import vn.trinhtung.dto.AuthorDto;
import vn.trinhtung.entity.Author;
import vn.trinhtung.repository.AuthorRepository;

@Component
public class AuthorValidator implements Validator {
	@Autowired
	private AuthorRepository authorRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(AuthorDto.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		AuthorDto authorDto = (AuthorDto) target;

		Optional<Author> findByName = authorRepository.findByName(authorDto.getName());

		if (authorDto.getId() == null) {
			if (findByName.isPresent()) {
				errors.rejectValue("name", null, "Tên tác giả đã tồn tại");
			}
		} else {
			if (findByName.isPresent() && authorDto.getId() != findByName.get().getId()) {
				errors.rejectValue("name", null, "Tên tác giả đã tồn tại");
			}
		}
	}

}
