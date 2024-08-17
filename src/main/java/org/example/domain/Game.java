package org.example.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

//@Getter
//@Setter
//@Entity
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
//@Table(name = "game")
public class Game {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_no")
    private Integer gameNo;

    @Column(name = "is_win")
    private Boolean isWin;

    @Column(name = "home_score")
    private Integer homeScore;

    @Column(name = "home_no")
    private Integer homeNo;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "home_no", insertable = false, updatable = false)
    private User home;

    @Column(name = "away_score")
    private Integer awayScore;

    @Column(name = "away_no")
    private Integer awayNo;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "away_no", insertable = false, updatable = false)
    private User away;

}
