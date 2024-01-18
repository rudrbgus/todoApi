package org.study.todoapi.todo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.todoapi.todo.dto.response.TodoDetailResponseDTO;
import org.study.todoapi.todo.dto.response.TodoListResponseDTO;
import org.study.todoapi.todo.entity.Todo;
import org.study.todoapi.todo.repository.TodoRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional // JPA 사용시 필수 (서비스에)
public class TodoService {
    private final TodoRepository todoRepository;

    // 할 일 등록
    public TodoListResponseDTO create(Todo todo){
        todoRepository.save(todo);
        log.info("새로운 할 일이 저장되었습니다. 제목: {}", todo.getTitle());
        return retrieve();
    }

    // 할 일 목록 불러오기
    public TodoListResponseDTO retrieve(){

        List<Todo> todoList = todoRepository.findAll();
        List<TodoDetailResponseDTO> collect = todoList.stream().map(TodoDetailResponseDTO::new).collect(Collectors.toList());
        return TodoListResponseDTO.builder()
                .todos(collect)
                .build();
    }

    // 할 일 삭제
    public TodoListResponseDTO delete(String id){
        try{
            todoRepository.deleteById(id);
        }catch (Exception e){
            log.error("id가 존재하지 않아 삭제에 실패했습니다. - ID: {}", id, e.getMessage());
            throw new RuntimeException("삭제에 실패했습니다.");
        }
        return retrieve();
    }
}
