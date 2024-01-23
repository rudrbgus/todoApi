package org.study.todoapi.user.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.study.todoapi.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {

    private String email;
    private String userName;

    @JsonFormat(pattern = "yyyy년 MM월 dd일")
    private LocalDate joinDate;

    private String token; // 로그인 인증토큰

    public LoginResponseDTO(User user, String token){
        this.email = user.getEmail();
        this.userName = user.getUserName();
        this.joinDate = LocalDate.from(user.getJoinDate());
        this.token = token;
    }


}
