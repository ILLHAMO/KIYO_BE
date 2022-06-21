package project.kiyobackend.store.domain.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,CategoryId> {

    Optional<Category> findById(CategoryId id);

    List<Category> findAll();
}
