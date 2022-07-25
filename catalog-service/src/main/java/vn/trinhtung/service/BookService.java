package vn.trinhtung.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import vn.trinhtung.dto.BookDto;

public interface BookService {
	BookDto getBySlugAndEnable(String slug);
	
	BookDto getBySlug(String slug);

	Page<BookDto> getAll(Integer page);

	Page<BookDto> getAllEnableByCategory(String categorySlug, Integer page);

	Page<BookDto> getAllEnable(Integer page);
	
	BookDto save(BookDto bookDto, MultipartFile image);
	
	void delete(Integer id);
	
	List<BookDto> getAllByIds(List<Integer> ids);
}
