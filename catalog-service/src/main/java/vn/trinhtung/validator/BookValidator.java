package vn.trinhtung.validator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import vn.trinhtung.dto.BookDto;
import vn.trinhtung.entity.Book;
import vn.trinhtung.repository.BookRepository;

@Component
public class BookValidator implements Validator {
	@Autowired
	private BookRepository bookRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(BookDto.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		BookDto bookDto = (BookDto) target;

		Optional<Book> findByName = bookRepository.findByName(bookDto.getName());
		Optional<Book> findBySlug = bookRepository.findByName(bookDto.getSlug());

		Integer id = bookDto.getId();

		if (id == null) {
			if (findByName.isPresent()) {
				errors.rejectValue("name", null, "Tên sách đã tồn tại");
			}
			if (findBySlug.isPresent()) {
				errors.rejectValue("slug", null, "Sách đã tồn tại");
			}
		} else {
			if (findByName.isPresent() && id != findByName.get().getId()) {
				errors.rejectValue("name", null, "Tên sách đã tồn tại");
			}
			if (findBySlug.isPresent() && id != findBySlug.get().getId()) {
				errors.rejectValue("slug", null, "Sách đã tồn tại");
			}
		}
	}

}
