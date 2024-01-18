package org.study.todoapi.todo.dto.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoListResponseDTO {

    private String error; // 만약 에러가 발생할 때 메세지를 저장할 것!
    private List<TodoDetailResponseDTO> todos;
}
