package vn.trinhtung.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.dto.AuthorDto;
import vn.trinhtung.entity.Author;
import vn.trinhtung.exception.ResourceNotFoundException;
import vn.trinhtung.repository.AuthorRepository;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
	private final AuthorRepository authorRepository;

	@Override
	public Page<AuthorDto> getAll(Integer page) {
		Sort sort = Sort.by("id").descending();
		Pageable pageable = PageRequest.of(page - 1, 10, sort);

		return authorRepository.findAll(pageable).map(
				author -> AuthorDto.builder().id(author.getId()).name(author.getName()).build());
	}

	@Override
	public List<AuthorDto> getAll() {
		return authorRepository.findAll().stream().map(
				author -> AuthorDto.builder().id(author.getId()).name(author.getName()).build())
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public AuthorDto save(AuthorDto authorDto) {
		Author saved = null;

		if (authorDto.getId() != null) {
			Author author = authorRepository.findById(authorDto.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Tác giả không tồn tại"));

			author.setName(authorDto.getName());
			saved = authorRepository.save(author);
		} else {
			saved = authorRepository.save(Author.builder().name(authorDto.getName()).build());
		}

		return AuthorDto.builder().id(saved.getId()).name(saved.getName()).build();
	}

	@Transactional
	@Override
	public void delete(Integer id) {
		try {
			authorRepository.deleteById(id);
		} catch (Exception e) {
			throw new ResourceNotFoundException("Tác giả không tồn tại");
		}
	}

}
