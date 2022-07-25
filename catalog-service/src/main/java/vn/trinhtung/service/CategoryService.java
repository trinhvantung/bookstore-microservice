package vn.trinhtung.service;

import java.util.List;

import vn.trinhtung.dto.CategoryDto;

public interface CategoryService {
	List<CategoryDto> getAllByParentId(Integer parentId);

	List<CategoryDto> getAll();
	
	CategoryDto save(CategoryDto categoryDto);
	
	void delete(Integer id);
}
