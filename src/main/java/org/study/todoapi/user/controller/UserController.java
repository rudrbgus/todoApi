package org.study.todoapi.user.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.study.todoapi.auth.TokenUserInfo;
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

    // 일반회원을 프리미엄으로 상승시키는 요청 처리
    @PutMapping("/promote")
    // 그냥 이 권한을 가진 사람만 이 요청을 수행할 수 있고
    // 이 권한이 아닌 유저는 강제로 403이 응답됨
    @PreAuthorize("hasRole('ROLE_COMMON')")
//    @PreAuthorize("hasRole('ROLE_COMMON') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> promote(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo
    ){
        log.info("/api/auth/promote PUT");
        try{
            LoginResponseDTO loginResponseDTO = userService.promoteToPremium(tokenUserInfo);
            return ResponseEntity.ok().body(loginResponseDTO);
        }catch (IllegalStateException e){
            e.printStackTrace();
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            log.warn(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

}
