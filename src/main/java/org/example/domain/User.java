package org.example.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Table(name = "user")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private Integer userNo;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "house")
    private String house;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "join_code", length = 64)
    private String joinCode;

    @Column(name = "point")
    private Integer point;

    @Column(name = "join_code_at", columnDefinition = "DATETIME")
    private LocalDateTime joinCodeAt;

    @Column(name = "created_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

}
