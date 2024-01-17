package org.study.todoapi.todo.dto.request;

import lombok.*;
import org.study.todoapi.todo.entity.Todo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter @Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoCreateRequestDTO {
    @NotBlank
    @Size(min = 2, max = 10)
    private String title;

    // 엔터티 변환 메서드
    public Todo toEntity(){
        return Todo.builder()
                .title(title)
                .build();
    }
}
