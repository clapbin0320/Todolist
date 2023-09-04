package web.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.todolist.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
