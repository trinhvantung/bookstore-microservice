package vn.trinhtung.validator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import vn.trinhtung.dto.CategoryDto;
import vn.trinhtung.entity.Category;
import vn.trinhtung.repository.CategoryRepository;

@Component
public class CategoryValidator implements Validator {
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(CategoryDto.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CategoryDto categoryRequest = (CategoryDto) target;

		Optional<Category> findByName = categoryRepository.findByName(categoryRequest.getName());
		Optional<Category> findBySlug = categoryRepository.findBySlug(categoryRequest.getSlug());

		Integer id = categoryRequest.getId();

		if (id == null) {
			if (findByName.isPresent()) {
				errors.rejectValue("name", null, "Tên danh mục đã tồn tại");
			}
			if (findBySlug.isPresent()) {
				errors.rejectValue("name", null, "Danh mục đã tồn tại");
			}
		} else {

			if (findByName.isPresent() && id != findByName.get().getId()) {
				errors.rejectValue("name", null, "Tên danh mục đã tồn tại");
			}
			if (findBySlug.isPresent() && id != findBySlug.get().getId()) {
				errors.rejectValue("name", null, "Danh mục đã tồn tại");
			}
		}
	}

}
