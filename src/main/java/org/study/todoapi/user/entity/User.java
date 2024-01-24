package org.study.todoapi.user.entity;


import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.study.todoapi.todo.entity.Todo;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter@Getter@NoArgsConstructor@AllArgsConstructor
@EqualsAndHashCode@ToString
@Builder
@Entity
@Table(name = "tbl_user")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id; // account가 아니라 랜덤 식별 번호

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userName;

    @CreationTimestamp
    private LocalDateTime joinDate;

    @Enumerated(EnumType.STRING)
    //@ColumnDefault("'COMMON'") -> 작동 안함
    @Builder.Default
    private Role role = Role.COMMON;

    @OneToMany(mappedBy = "user") // <= 원 투 매니는 LAZY가 기본값
    private List<Todo> todo = new ArrayList<>();
}
