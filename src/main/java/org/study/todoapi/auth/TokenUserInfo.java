package org.study.todoapi.auth;

import lombok.*;
import org.study.todoapi.user.entity.Role;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class TokenUserInfo {

    private String userId;
    private String email;
    private Role role;


}
