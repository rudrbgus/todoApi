package org.study.todoapi.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.todoapi.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    // 쿼리 메서드
    // 이메일로 회원정보 조회
    Optional<User> findByEmail(String email);

    // 이메일 중복체크
    boolean existsByEmail(String email);
}
