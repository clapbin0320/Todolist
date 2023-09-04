package web.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.todolist.domain.Memo;

public interface MemoRepository extends JpaRepository<Memo, Long> {
}
