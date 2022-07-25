package vn.trinhtung.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.trinhtung.dto.ReviewDto;
import vn.trinhtung.service.ReviewService;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
	@Autowired
	private ReviewService reviewService;

	@PreAuthorize("isAuthenticated()")
	@SuppressWarnings("unchecked")
	@PostMapping
	public ResponseEntity<?> create(OAuth2Authentication authentication,
			@RequestBody(required = false) ReviewDto review) {
		Map<String, Object> details = (Map<String, Object>) authentication.getUserAuthentication()
				.getDetails();

		Map<String, Object> user = (Map<String, Object>) ((Map<String, Object>) details
				.get("principal")).get("user");

		return ResponseEntity.ok(reviewService.save(review, user));
	}

	@GetMapping("/{bookId}")
	public ResponseEntity<?> getAllByBookId(@PathVariable Integer bookId,
			@RequestParam(defaultValue = "1") Integer page) {

		return ResponseEntity.ok(reviewService.getAllByBookId(bookId, page));
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/current/{bookId}")
	public ResponseEntity<?> checkReview(@PathVariable Integer bookId,
			OAuth2Authentication authentication) {
		Map<String, Object> details = (Map<String, Object>) authentication.getUserAuthentication()
				.getDetails();

		Map<String, Object> user = (Map<String, Object>) ((Map<String, Object>) details
				.get("principal")).get("user");

		return ResponseEntity
				.ok(reviewService.getCurrentReviewByBookId(bookId, (Integer) user.get("id")));
	}

	@GetMapping("/details/{bookId}")
	public ResponseEntity<?> getDetailsByBookId(@PathVariable Integer bookId) {

		return ResponseEntity.ok(reviewService.getDetailsByBookId(bookId));
	}
}
