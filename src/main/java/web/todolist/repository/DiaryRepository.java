package web.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.todolist.domain.Diary;
import web.todolist.domain.User;

import java.time.LocalDate;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Optional<Diary> findByUserAndDate(User user, LocalDate date);
}
