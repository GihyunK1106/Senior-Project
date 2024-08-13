package org.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.example.utils.Crypt;
import org.example.utils.keys.SESSION;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Table(name = "user_session")
public class UserSession {

    @Id
    @Column(name = "user_no")
    private Integer userNo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_no", insertable = false, updatable = false)
    private User user;

    @Column(name = "salt")
    private String salt;

    @Column(name = "session_key")
    private String sessionKey;

    @Column(name = "created_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;


    public void setUser(User user) {
        this.user = user;
        this.userNo = user.getUserNo();
    }

//    public void setSessionTypes(UserSessionTypes sessionTypes) {
//        this.sessionTypes = sessionTypes;
//        if(bePresent(this.sessionTypes)){
//            this.sessionTypeCode = this.sessionTypes.getSession();
//        }
//    }

    @JsonIgnore
    public void makeSessionKey(){
        User users = this.getUser();
        this.salt = Crypt.newCrypt().getSalt(8);
        try {
            this.sessionKey = Jwts.builder()
                    .setIssuer(SESSION.TOKEN_ISSURE)
                    .setSubject(SESSION.TOKEN_NAME)
                    .claim("email", users.getEmail())
                    .claim("sess_now", LocalDateTime.now().toString())
                    .setIssuedAt(new Date())
                    .signWith(
                            SignatureAlgorithm.HS256,
                            this.salt.getBytes("UTF-8")
                    ).compact();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
//            withException("120-002");
        }
    }
}
