package vn.trinhtung.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.dto.ReviewDto;
import vn.trinhtung.dto.UserDto;
import vn.trinhtung.entity.Review;
import vn.trinhtung.exception.ResourceAlreadyExistsException;
import vn.trinhtung.feign.UserFeignClient;
import vn.trinhtung.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final ReviewRepository reviewRepository;
	private final UserFeignClient userFeignClient;
	private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

	@Override
	public ReviewDto save(ReviewDto reviewDto, Map<String, Object> user) {
		UserDto userDto = UserDto.builder().id((Integer) user.get("id"))
				.fullname((String) user.get("fullname")).build();

		if (reviewRepository.findByBookIdAndUserId(reviewDto.getBookId(), userDto.getId())
				.isPresent()) {
			throw new ResourceAlreadyExistsException("Đã nhận xét");
		}

		Review saved = reviewRepository
				.save(Review.builder().star(reviewDto.getStar()).content(reviewDto.getContent())
						.bookId(reviewDto.getBookId()).userId(userDto.getId()).build());

		return ReviewDto.builder().star(saved.getStar()).id(saved.getId())
				.content(saved.getContent()).bookId(saved.getBookId()).user(userDto).build();
	}

	@Override
	public Page<ReviewDto> getAllByBookId(Integer bookId, Integer page) {
		Sort sort = Sort.by("id").descending();
		Pageable pageable = PageRequest.of(page - 1, 3, sort);

		Page<Review> pageReview = reviewRepository.findAllByBookId(bookId, pageable);
		List<Integer> userIds = pageReview.getContent().stream().map(review -> review.getUserId())
				.collect(Collectors.toList());

		CircuitBreaker circuitBreaker = circuitBreakerFactory.create("id");
//		List<UserDto> users = userFeignClient.getUserByIds(userIds);
		List<UserDto> users = circuitBreaker.run(() -> userFeignClient.getUserByIds(userIds),
				t -> Arrays.asList());

		return pageReview.map(review -> ReviewDto.builder().id(review.getId())
				.content(review.getContent()).bookId(review.getBookId()).star(review.getStar())
				.user(users.stream().filter(us -> us.getId().equals(review.getUserId())).findFirst()
						.orElse(null))
				.build());
	}

	@Override
	public ReviewDto getDetailsByBookId(Integer bookId) {
		List<Object[]> objects = reviewRepository.findDetailsByBookId(bookId);
		Map<Byte, Long> details = objects.stream()
				.collect(Collectors.toMap(object -> (Byte) object[0], object -> (Long) object[1]));

		for (Byte i = 1; i <= 5; i++) {
			if (!details.containsKey(i)) {
				details.put(i, 0l);
			}
		}

		return ReviewDto.builder().details(details).build();
	}

	@Override
	public ReviewDto getCurrentReviewByBookId(Integer bookId, Integer userId) {

		return reviewRepository.findByBookIdAndUserId(bookId, userId).map(
				review -> ReviewDto.builder().id(review.getId()).star(review.getStar()).build())
				.orElse(null);
	}

}
