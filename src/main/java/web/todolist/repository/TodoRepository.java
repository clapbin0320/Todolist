package web.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.todolist.domain.Category;
import web.todolist.domain.Todo;
import web.todolist.domain.User;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("select t from Todo t where t.user = :user and t.date = :date")
    List<Todo> getTodoList(@Param("user") User user, @Param("date") LocalDate date);

    List<Todo> findByCategory(Category category);
}
