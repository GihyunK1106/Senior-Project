package org.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "user_point_histories")
public class UserPointHistories {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_no")
    private Long historyNo;

    @Column(name = "user_no")
    private Integer userNo;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_no", insertable = false, updatable = false)
    private User user;

    @Column(name = "point")
    private Integer point;

    @Column(name = "created_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

}
