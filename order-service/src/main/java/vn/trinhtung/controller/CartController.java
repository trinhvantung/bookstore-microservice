package vn.trinhtung.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.trinhtung.dto.CartItemDto;
import vn.trinhtung.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	@Autowired
	private CartService cartService;

	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public ResponseEntity<?> getAll() {

		return ResponseEntity.ok(cartService.getAllByUser());
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping
	public ResponseEntity<?> add(@RequestBody CartItemDto cartItemDto) {

		return ResponseEntity.ok(cartService.add(cartItemDto));
	}

	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		cartService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("isAuthenticated()")
	@PutMapping("/{id}/{quantity}")
	public ResponseEntity<?> update(@PathVariable Integer id, @PathVariable Integer quantity) {
		return ResponseEntity.ok(cartService.update(id, quantity));
	}

}
