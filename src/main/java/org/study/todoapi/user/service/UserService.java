package org.study.todoapi.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.todoapi.exception.DuplicateEmailException;
import org.study.todoapi.exception.NoRegisteredArgumentsException;
import org.study.todoapi.user.dto.request.UserSignUpRequestDTO;
import org.study.todoapi.user.dto.response.UserSignUpResponseDTO;
import org.study.todoapi.user.entity.User;
import org.study.todoapi.user.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // 회원가입 처리
    public UserSignUpResponseDTO create(UserSignUpRequestDTO dto) {
        if (dto == null) {
            throw new NoRegisteredArgumentsException("회원가입 입력정보가 없습니다!");
        }
        String email = dto.getEmail();
        if(userRepository.existsByEmail(email)){
            log.warn("이메일이 중복되었습니다!! -{}.", email);
            throw new DuplicateEmailException("중복된 이메일입니다!!");
        }

        User saved = userRepository.save(dto.toEntity(passwordEncoder));
        log.info("회원가입 성공!! saved user - {}", saved);

        return new UserSignUpResponseDTO(saved);//회원가입 정보를 클라이언트에게 리턴
    }

    // 이메일 중복 확인
    public boolean isDuplicateEmail(String email){
        return userRepository.existsByEmail(email);
    }

}
