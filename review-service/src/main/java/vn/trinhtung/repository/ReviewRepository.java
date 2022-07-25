package vn.trinhtung.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.trinhtung.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
	Page<Review> findAllByBookId(Integer bookId, Pageable pageable);
	
	@Query("SELECT r.star, COUNT(r) FROM Review r WHERE r.bookId = :bookId GROUP BY r.star")
	List<Object[]> findDetailsByBookId(Integer bookId);
	
	Optional<Review> findByBookIdAndUserId(Integer bookId, Integer userId);
}
