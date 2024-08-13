package org.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import org.example.object.OriginObject;
import org.example.object.exceptions.BusinessException;
import org.example.utils.Crypt;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Table(name = "user_pw")
public class UserPw extends OriginObject {

    @Id
    @Column(name = "user_no")
    private Integer userNo;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_no", insertable = false, updatable = false)
    private User user;

    @Column(name = "salt")
    private String salt;

    @Column(name = "password")
    private String password;

    @Column(name = "bak_salt")
    private String bakSalt;

    @Column(name = "bak_password")
    private String bakPassword;

    @Column(name = "updated_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;


    public void makePassword(String _password){
        String salt = Crypt.newCrypt().getSalt(128);
        String password = Crypt.newCrypt().getPassword(_password, salt);
        this.setPassword(password);
        this.setSalt(salt);
    }

    public UserPw loginWithPassword(String password){
        // todo: 이후 삭제
//        password = Crypt.newCrypt().SHA256(password);
        String requestedPwd = Crypt.newCrypt().getPassword(password, this.salt);
        if(!bePresent(requestedPwd) || !bePresent(this.password) || !requestedPwd.equals(this.password))
            throw new BusinessException("패스워드를 잘못 입력하셨습니다.", HttpStatus.BAD_REQUEST);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
        this.userNo = user.getUserNo();
    }
}
