package org.study.todoapi.todo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.todoapi.todo.dto.request.TodoCheckRequestDTO;
import org.study.todoapi.todo.dto.request.TodoCreateRequestDTO;
import org.study.todoapi.todo.dto.response.TodoDetailResponseDTO;
import org.study.todoapi.todo.dto.response.TodoListResponseDTO;
import org.study.todoapi.todo.entity.Todo;
import org.study.todoapi.todo.repository.TodoRepository;
import org.study.todoapi.user.entity.User;
import org.study.todoapi.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional // JPA 사용시 필수 (서비스에)
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    // 할 일 등록
    public TodoListResponseDTO create(TodoCreateRequestDTO dto, String email){

        Optional<User> foundUser = userRepository.findByEmail(email);

        foundUser.ifPresent(user -> {
            Todo save = todoRepository.save(dto.toEntity(user));
            // 양방향 매핑에서는 한쪽이 수정(삽입, 삭제)되면 반대편에는 수동으로 갱신을 해줘야 함
            user.addTodo(save);
        });

        log.info("새로운 할 일이 저장되었습니다. 제목: {}", dto.getTitle());
        return retrieve(email);
    }

    // 할 일 목록 불러오기
    public TodoListResponseDTO retrieve(String email){

        // 양 방향 활용해서 가져오기
        User user = userRepository.findByEmail(email).orElseThrow();

        List<Todo> todoList = user.getTodoList();

//        List<Todo> todoList = todoRepository.findAll();
        List<TodoDetailResponseDTO> collect = todoList.stream().map(TodoDetailResponseDTO::new).collect(Collectors.toList());
        return TodoListResponseDTO.builder()
                .todos(collect)
                .build();
    }

    // 할 일 삭제
    public TodoListResponseDTO delete(String id, String email){
        try{
            todoRepository.deleteById(id);
        }catch (Exception e){
            log.error("id가 존재하지 않아 삭제에 실패했습니다. - ID: {}", id, e.getMessage());
            throw new RuntimeException("삭제에 실패했습니다.");
        }
        return retrieve(email);
    }

    // 할 일 체크 처리
    public TodoListResponseDTO check(TodoCheckRequestDTO dto, String email){
        Optional<Todo> target = todoRepository.findById(dto.getId());
        target.ifPresent(todo ->{
            todo.setDone(dto.isDone());
            todoRepository.save(todo);
        });

        return retrieve(email);
    }
}
