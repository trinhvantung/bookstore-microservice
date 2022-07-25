package vn.trinhtung.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.dto.BookDto;
import vn.trinhtung.service.BookService;
import vn.trinhtung.validator.BookValidator;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {
	private final BookService bookService;
	private final BookValidator bookValidator;

	@GetMapping("/{slug}")
	public ResponseEntity<?> getEnable(@PathVariable String slug) {

		return ResponseEntity.ok(bookService.getBySlugAndEnable(slug));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/all")
	public ResponseEntity<?> getAll(@RequestParam(defaultValue = "1") Integer page) {

		return ResponseEntity.ok(bookService.getAll(page));
	}

	@GetMapping("/category/{slug}")
	public ResponseEntity<?> getAllEnableByCategory(@RequestParam(defaultValue = "1") Integer page,
			@PathVariable String slug) {

		return ResponseEntity.ok(bookService.getAllEnableByCategory(slug, page));
	}

	@GetMapping
	public ResponseEntity<?> getAllEnable(@RequestParam(defaultValue = "1") Integer page) {

		return ResponseEntity.ok(bookService.getAllEnable(page));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestPart(name = "book") BookDto bookDto,
			@RequestPart MultipartFile image, BindingResult bindingResult) throws BindException {
		bookDto.setId(null);
		bookValidator.validate(bookDto, bindingResult);

		if (image.getOriginalFilename().isBlank()) {
			bindingResult.addError(new FieldError("thumnail", "thumnail", "Chưa upload ảnh"));
		}

		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(bookService.save(bookDto, image));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id,
			@Valid @RequestPart(name = "book") BookDto bookDto,
			@RequestPart(required = false, name = "image") MultipartFile image,
			BindingResult bindingResult) throws BindException {
		bookDto.setId(id);
		bookValidator.validate(bookDto, bindingResult);

		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(bookService.save(bookDto, image));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		bookService.delete(id);

		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("#oauth2.hasScope('server')")
	@GetMapping("/ids")
	public ResponseEntity<?> getAllByIds(@RequestParam List<Integer> ids) {

		return ResponseEntity.ok(bookService.getAllByIds(ids));
	}
}
