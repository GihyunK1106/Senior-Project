package org.example.repository;

import org.example.domain.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionRepository extends JpaRepository<UserSession, Integer> {
    void deleteAllByUserNo(Integer userNo);

    UserSession findByUser_Email(String email);
}
