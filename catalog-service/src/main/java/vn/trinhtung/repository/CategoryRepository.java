package vn.trinhtung.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.trinhtung.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	Optional<Category> findByName(String name);

	Optional<Category> findBySlug(String slug);

	@Query("SELECT c.id, c.name, c.slug, c.parent.id FROM Category c")
	List<Object[]> findAllCategory();

	@Query("SELECT c.id, c.name, c.slug, c.parent.id FROM Category c "
			+ "WHERE (c.parent.id is null AND :parentId = null) OR (c.parent.id = :parentId)")
	List<Object[]> findAllByParentId(Integer parentId);
}
