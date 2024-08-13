package org.example.config;

import org.example.object.exceptions.BusinessException;
import org.example.service.internal.Workspace;
import org.example.utils.keys.ENV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandlers extends ResponseEntityExceptionHandler {

    @Autowired
    private Workspace workspace;

    @ExceptionHandler
    protected ResponseEntity<Object> handleConflict(RuntimeException exception, WebRequest request) {
        if(!ENV.isRelease){
            exception.printStackTrace();
        }
        Map errorResponse = new HashMap();
        if(exception instanceof BusinessException){
            BusinessException businessException = (BusinessException) exception;
            extracted(businessException);
            errorResponse.put("error_code", businessException.getErr_code());
            errorResponse.put("message", extracted(businessException));
            errorResponse.put("status", businessException.getErrHttpStatus());
            return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), businessException.getErrHttpStatus(), request);
        }
        return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    private String extracted(BusinessException businessException) {
        Map map = new HashMap();
        map.put("lang_code", businessException.getErr_code() == null ? "" : businessException.getErr_code());
        map.put("lang", "kr");
        String message = (String)workspace.getItem("comm.common.getMessage", map);
        return message == null ? "" : message;
    }

}