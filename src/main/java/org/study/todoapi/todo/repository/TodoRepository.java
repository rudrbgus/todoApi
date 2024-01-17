package org.study.todoapi.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.todoapi.todo.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo, String> {
}
