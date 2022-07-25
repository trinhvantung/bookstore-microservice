package vn.trinhtung.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.dto.CategoryDto;
import vn.trinhtung.entity.Category;
import vn.trinhtung.exception.ResourceNotFoundException;
import vn.trinhtung.repository.CategoryRepository;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
	private final CategoryRepository categoryRepository;

	@Override
	public List<CategoryDto> getAllByParentId(Integer parentId) {

		return categoryRepository.findAllByParentId(parentId).stream()
				.map(object -> CategoryDto.builder().id((Integer) object[0])
						.name((String) object[1]).slug((String) object[2])
						.parent(object[3] == null ? null
								: CategoryDto.builder().id((Integer) object[3]).build())
						.build())
				.collect(Collectors.toList());
	}

	@Override
	public List<CategoryDto> getAll() {

		return categoryRepository.findAllCategory().stream()
				.map(object -> CategoryDto.builder().id((Integer) object[0])
						.name((String) object[1]).slug((String) object[2])
						.parent(object[3] == null ? null
								: CategoryDto.builder().id((Integer) object[3]).build())
						.build())
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public CategoryDto save(CategoryDto categoryDto) {
		Category saved = null;

		if (categoryDto.getId() != null) {
			Category category = categoryRepository.findById(categoryDto.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Danh mục không tồn tại"));
			category.setName(categoryDto.getName());
			category.setSlug(categoryDto.getSlug());
			category.setParent(Category.builder().id(categoryDto.getId()).build());

			saved = categoryRepository.save(category);
		} else {
			Category category = new Category();
			category.setName(categoryDto.getName());
			category.setSlug(categoryDto.getSlug());

			saved = categoryRepository.save(category);
		}

		return CategoryDto.builder().id(saved.getId()).name(saved.getName()).slug(saved.getSlug())
				.build();
	}

	@Transactional
	@Override
	public void delete(Integer id) {
		try {
			categoryRepository.deleteById(id);
		} catch (Exception e) {
			throw new ResourceNotFoundException("Danh mục không tồn tại");
		}
	}

}
