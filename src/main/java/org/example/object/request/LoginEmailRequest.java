package org.example.object.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import org.example.object.OriginObject;
import org.example.object.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoginEmailRequest extends OriginObject {
    private String email;
    private String password;

    public void checkValidation(){
        if(!bePresent(this.email)) new BusinessException("이메일을 입렵해주세요", HttpStatus.BAD_REQUEST);
        if(!bePresent(this.password)) new BusinessException("패스워드를 입렵해주세요", HttpStatus.BAD_REQUEST);
    }

}
