package web.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.todolist.domain.Todolist;

public interface TodolistRepository extends JpaRepository<Todolist, Long> {
}
