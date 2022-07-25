package vn.trinhtung.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.dto.AuthorDto;
import vn.trinhtung.service.AuthorService;
import vn.trinhtung.validator.AuthorValidator;

@RestController
@RequestMapping("/api/author")
@RequiredArgsConstructor
public class AuthorController {
	private final AuthorService authorService;
	private final AuthorValidator authorValidator;

	@GetMapping
	public ResponseEntity<?> getAll() {

		return ResponseEntity.ok(authorService.getAll());
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/all")
	public ResponseEntity<?> getAll(@RequestParam(defaultValue = "1") Integer page) {

		return ResponseEntity.ok(authorService.getAll(page));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody AuthorDto author,
			BindingResult bindingResult) throws BindException {
		author.setId(null);

		authorValidator.validate(author, bindingResult);
		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}

		return ResponseEntity.ok(authorService.save(author));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody AuthorDto author,
			BindingResult bindingResult) throws BindException {
		author.setId(id);

		authorValidator.validate(author, bindingResult);
		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}

		return ResponseEntity.ok(authorService.save(author));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		authorService.delete(id);

		return ResponseEntity.noContent().build();
	}
}
