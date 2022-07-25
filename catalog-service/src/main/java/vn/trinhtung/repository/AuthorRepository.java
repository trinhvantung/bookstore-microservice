package vn.trinhtung.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.trinhtung.entity.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
	Optional<Author> findByName(String name);
}
