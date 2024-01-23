package org.study.todoapi.user.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.study.todoapi.exception.DuplicateEmailException;
import org.study.todoapi.exception.NoRegisteredArgumentsException;
import org.study.todoapi.user.dto.request.LoginRequestDTO;
import org.study.todoapi.user.dto.request.UserSignUpRequestDTO;
import org.study.todoapi.user.dto.response.LoginResponseDTO;
import org.study.todoapi.user.dto.response.UserSignUpResponseDTO;
import org.study.todoapi.user.service.UserService;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    // 회원가입 요청처리
    @PostMapping
    public ResponseEntity<?> signUp(
            @Validated @RequestBody UserSignUpRequestDTO dto
            , BindingResult result) {
        log.info("/api/auth POST! - {}", dto);

        if(result.hasErrors()){
            log.warn(result.toString());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        try{
            UserSignUpResponseDTO responseDTO = userService.create(dto);
            return ResponseEntity.ok().body(responseDTO);
        }catch (NoRegisteredArgumentsException e){
            log.warn("필수 가입 정보를 전달받지 못했습니다!!");
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (DuplicateEmailException e){
            log.warn("이메일이 중복되었습니다.");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 이메일 중복확인 요청처리
    @GetMapping("/check")
    public ResponseEntity<?> check(String email){
        boolean flag = userService.isDuplicateEmail(email);
        log.debug("{} 중복?? -{}", email, flag);

        return ResponseEntity.ok().body(flag);
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Validated @RequestBody LoginRequestDTO dto, BindingResult result){
        try{
            LoginResponseDTO authenticate = userService.authenticate(dto);
            log.info("login success !! by {}", authenticate.getEmail());
            return ResponseEntity.ok().body(authenticate);
        }catch (RuntimeException e){
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
