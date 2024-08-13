package org.example.object.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import org.example.object.OriginObject;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SessionResponse extends OriginObject {

    private String userName;
    private String email;
    private String sessionKey;

//    public void setUserStatusType(UserStatusTypes userStatusType) {
//        this.userStatusType = userStatusType;
//        this.userStatusTypeCode = userStatusType.getUserStatus();
//    }

}
