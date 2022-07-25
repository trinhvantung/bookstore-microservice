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
import vn.trinhtung.dto.CategoryDto;
import vn.trinhtung.service.CategoryService;
import vn.trinhtung.validator.CategoryValidator;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryService categoryService;
	private final CategoryValidator categoryValidator;

	@GetMapping
	public ResponseEntity<?> getAll() {

		return ResponseEntity.ok(categoryService.getAll());
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/all")
	public ResponseEntity<?> getAll(
			@RequestParam(required = false, name = "parent-id") Integer parentId) {

		return ResponseEntity.ok(categoryService.getAllByParentId(parentId));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody CategoryDto category,
			BindingResult bindingResult) throws BindException {
		category.setId(null);

		categoryValidator.validate(category, bindingResult);
		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}

		return ResponseEntity.ok(categoryService.save(category));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id,
			@Valid @RequestBody CategoryDto category, BindingResult bindingResult)
			throws BindException {
		category.setId(id);

		categoryValidator.validate(category, bindingResult);
		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}

		return ResponseEntity.ok(categoryService.save(category));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		categoryService.delete(id);

		return ResponseEntity.noContent().build();
	}
}
