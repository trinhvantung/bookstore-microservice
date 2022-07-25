package vn.trinhtung.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.trinhtung.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
	Optional<Book> findByName(String name);

	Optional<Book> findBySlug(String slug);
	
	Optional<Book> findBySlugAndEnableTrue(String slug);
	
	Page<Book> findAllByCategorySlugAndEnableTrue(String categorySlug, Pageable pageable);

	Page<Book> findAllByEnableTrue(Pageable pageable);
}
