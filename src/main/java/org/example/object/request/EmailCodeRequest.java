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
public class EmailCodeRequest extends OriginObject {

    private String email;
    private String code;

    public void checkValidation(){
        if(!bePresent(code)) new BusinessException("code를 입렵해주세요", HttpStatus.BAD_REQUEST);
    }

}
