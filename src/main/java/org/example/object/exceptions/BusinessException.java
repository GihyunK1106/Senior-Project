package org.example.object.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class BusinessException extends RuntimeException implements AutoCloseable {

    private String msg;

    private String err_code;

    private HttpStatus errHttpStatus = HttpStatus.BAD_REQUEST;

    @JsonIgnore
    private Boolean fromDatabase = true;

    public BusinessException(){}

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String msg, HttpStatus httpStatus, String err_code){
        this.msg = msg;
        this.errHttpStatus = httpStatus;
        this.err_code = err_code;
    }

    public BusinessException(String err_code, HttpStatus httpStatus){
        this.errHttpStatus = httpStatus;
        this.err_code = err_code;
    }

    @Override
    public void close() throws Exception {

    }
}