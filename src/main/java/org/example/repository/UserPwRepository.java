package org.example.repository;

import org.example.domain.UserPw;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPwRepository extends JpaRepository<UserPw, Integer> {
    UserPw findByUser_Email(String email);

}
