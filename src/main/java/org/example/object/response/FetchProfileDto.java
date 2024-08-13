package org.example.object.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FetchProfileDto {

    private String name;
    private String gender;
    private String birth;
    private String email;
    private String pairedCode;
    private String password;
}
