package vn.trinhtung.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.trinhtung.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	Page<Order> findAllByUserId(Integer userId, Pageable pageable);
	
	Optional<Order> findByIdAndUserId(Integer id, Integer userId);
	
	@EntityGraph(attributePaths = {"orderItems"})
	@Query("SELECT o FROM Order o WHERE o.id = :id")
	Optional<Order> findByIdJoinFetchOrderItemsAndOrderTracks(Integer id);
}
