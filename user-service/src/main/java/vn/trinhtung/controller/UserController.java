package vn.trinhtung.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.dto.UserDto;
import vn.trinhtung.service.UserService;
import vn.trinhtung.validator.RegisterUserValidator;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	private final RegisterUserValidator registerUserValidator;
	
	@PostMapping("/register")
	public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto user,
			BindingResult bindingResult) throws BindException {
		registerUserValidator.validate(user, bindingResult);

		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(user));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping
	public ResponseEntity<Page<UserDto>> getAll(@RequestParam(defaultValue = "1") Integer page) {

		return ResponseEntity.ok(userService.getAll(page));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/demo")
	public ResponseEntity<?> getAlll(OAuth2Authentication authentication) {
		System.out.println("DEMO");
		return ResponseEntity.ok(authentication);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		userService.deleteById(id);

		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("#oauth2.hasScope('server')")
	@GetMapping("/ids")
	public ResponseEntity<?> getUserByIds(@RequestParam List<Integer> ids, Principal principal,
			HttpServletRequest request) {
		if (principal != null) {
			System.out.println(principal.toString());
		}
		return ResponseEntity.ok(userService.getByIds(ids));
	}

}
