package org.study.todoapi.todo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.todoapi.todo.entity.Todo;
import org.study.todoapi.todo.repository.TodoRepository;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional // JPA 사용시 필수 (서비스에)
public class TodoService {
    private final TodoRepository todoRepository;

    // 할 일 등록
    public void create(Todo todo){
        todoRepository.save(todo);
        log.info("새로운 할 일이 저장되었습니다. 제목: {}", todo.getTitle());
    }

}
