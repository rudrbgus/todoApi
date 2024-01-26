package org.study.todoapi.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.todoapi.auth.TokenProvider;
import org.study.todoapi.auth.TokenUserInfo;
import org.study.todoapi.exception.DuplicateEmailException;
import org.study.todoapi.exception.NoRegisteredArgumentsException;
import org.study.todoapi.user.dto.request.LoginRequestDTO;
import org.study.todoapi.user.dto.request.UserSignUpRequestDTO;
import org.study.todoapi.user.dto.response.LoginResponseDTO;
import org.study.todoapi.user.dto.response.UserSignUpResponseDTO;
import org.study.todoapi.user.entity.Role;
import org.study.todoapi.user.entity.User;
import org.study.todoapi.user.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;


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

    // 회원 인증( login )
    public LoginResponseDTO authenticate(final LoginRequestDTO dto){  // 안정성 추가

        // 이메일을 통해 회원정보 조회
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(
                () -> new RuntimeException("가입된 회원이 아닙니다!")
        );

        // 패스워드 검증
        String inputPassword = dto.getPassword(); // 입력 비번
        String encodedPassword = user.getPassword();

        if(!passwordEncoder.matches(inputPassword, encodedPassword)){
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

        // 로그인 성공 후 이제 어떻게 할 것인가 세션에 저장할 것인가 토큰을 발급할 것인가.
        String token = tokenProvider.createToken(user);

        // 클라이언트에게 토큰을 발급해서 제공
        return new LoginResponseDTO(user, token);

    }

    // 등급 업 처리
    public LoginResponseDTO promoteToPremium(TokenUserInfo userInfo){
        User user = userRepository.findByEmail(userInfo.getEmail()).orElseThrow(() -> new NoRegisteredArgumentsException("가입된 회원이 아닙니다."));
        // 이미 프리미엄 회원이거나 관리자면 예외 발생
//        if(user.getRole() != Role.COMMON) throw new IllegalStateException("일반 회원이 아니면 승격할 수 없습니다.");
        // 등급 변경
        user.setRole(Role.PREMIUM);

        // 다시 저장
        User saved = userRepository.save(user);

        // 토큰을 재발급
        String token = tokenProvider.createToken(user);


        return new LoginResponseDTO(saved,token);
    }

}
