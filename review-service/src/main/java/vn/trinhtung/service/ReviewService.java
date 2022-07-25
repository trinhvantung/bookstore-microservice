package vn.trinhtung.service;

import java.util.Map;

import org.springframework.data.domain.Page;

import vn.trinhtung.dto.ReviewDto;

public interface ReviewService {
	ReviewDto save(ReviewDto reviewDto, Map<String, Object> user);

	Page<ReviewDto> getAllByBookId(Integer bookId, Integer page);
	
	ReviewDto getDetailsByBookId(Integer bookId);
	
	ReviewDto getCurrentReviewByBookId(Integer bookId, Integer userId);
}
