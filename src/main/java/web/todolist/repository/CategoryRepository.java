package web.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.todolist.domain.Category;
import web.todolist.domain.User;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByUserAndCategoryName(User user, String categoryName);

    List<Category> findByUser(User user);

    Optional<Category> findByCategoryName(String categoryName);
}
