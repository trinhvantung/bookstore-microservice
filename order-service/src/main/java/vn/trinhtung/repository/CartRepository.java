package vn.trinhtung.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import vn.trinhtung.entity.CartItem;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Integer> {
	Optional<CartItem> findByUserIdAndBookId(Integer userId, Integer bookId);

	@Modifying
	void deleteByIdAndUserId(Integer id, Integer userId);

	List<CartItem> findAllByUserId(Integer userId);

	Optional<CartItem> findByIdAndUserId(Integer id, Integer userId);
	
	@Modifying
	void deleteAllByUserId(Integer userId);
}
