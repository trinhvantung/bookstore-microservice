package vn.trinhtung.service;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.trinhtung.dto.AuthorDto;

public interface AuthorService {
	Page<AuthorDto> getAll(Integer page);

	List<AuthorDto> getAll();
	
	AuthorDto save(AuthorDto authorDto);
	
	void delete(Integer id);
}
