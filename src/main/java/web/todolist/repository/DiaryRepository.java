package web.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.todolist.domain.Diary;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
