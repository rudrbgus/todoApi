package org.study.todoapi.todo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.study.todoapi.todo.dto.request.TodoCreateRequestDTO;
import org.study.todoapi.todo.entity.Todo;
import org.study.todoapi.todo.service.TodoService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;


    // 할 일 등록 요청
    @PostMapping
    private ResponseEntity<?> createTodo(@RequestBody TodoCreateRequestDTO dto){

        todoService.create(dto.toEntity());

        return ResponseEntity.ok().body("ok");
    }



}
