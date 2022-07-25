package vn.trinhtung.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.trinhtung.dto.OrderDto;
import vn.trinhtung.dto.OrderTrackDto;
import vn.trinhtung.service.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	@Autowired
	private OrderService orderService;

	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public ResponseEntity<?> getAllByUserId(@RequestParam(defaultValue = "1") Integer page) {

		return ResponseEntity.ok(orderService.getAllByUserId(page));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/all")
	public ResponseEntity<?> getAll(@RequestParam(defaultValue = "1") Integer page) {

		return ResponseEntity.ok(orderService.getAll(page));
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/{id}")
	public ResponseEntity<?> getByIdAndUserId(@PathVariable Integer id) {

		return ResponseEntity.ok(orderService.getByIdAndUser(id));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/detail/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {

		return ResponseEntity.ok(orderService.getById(id));
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody OrderDto orderDto) {

		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(orderDto));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping
	public ResponseEntity<?> updateOrderTrack(@Valid @RequestBody OrderTrackDto orderTrackDto) {

		return ResponseEntity.ok(orderService.updateOrderTrack(orderTrackDto));
	}
}
