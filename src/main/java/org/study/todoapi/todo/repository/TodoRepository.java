package org.study.todoapi.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.study.todoapi.todo.entity.Todo;
import org.study.todoapi.user.entity.User;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, String> {

    @Query("SELECT t FROM Todo t WHERE t.user = ?1")
    List<Todo> findAllByUser(User user);
}
