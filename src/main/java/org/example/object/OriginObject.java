package org.example.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.example.object.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public class OriginObject {

    @JsonIgnore
    private final static Gson gson = new Gson();

    @JsonIgnore
    protected String getJson(Object o){
        try{
            return gson.toJson(o);
        }catch (Exception e){
            return "";
        }
    }

    @JsonIgnore
    protected Gson getGsonInstance(){
        return gson;
    }

    @JsonIgnore
    protected void withStringException(String langCode){
        withStringException(langCode, HttpStatus.BAD_REQUEST);
    }

    @JsonIgnore
    protected void withStringException(String langCode, HttpStatus httpStatus){
        BusinessException businessException = new BusinessException(langCode, httpStatus);
        businessException.setFromDatabase(false);
        throw businessException;
    }

    @JsonIgnore
    protected void withException(String langCode){
        withException(langCode, HttpStatus.BAD_REQUEST);
    }

    @JsonIgnore
    protected void withException(String langCode, HttpStatus httpStatus){
        throw new BusinessException(langCode, httpStatus);
    }

    protected void writeMessage(String message){
        writeMessage(message, HttpStatus.BAD_REQUEST);
    }

    protected void writeMessage(String message, HttpStatus httpStatus){
        BusinessException businessException = new BusinessException();
        businessException.setErr_code("");
        businessException.setMsg(message);
        businessException.setErrHttpStatus(httpStatus);
        throw businessException;
    }

    @JsonIgnore
    protected <T> boolean bePresent(T obj){
        if(obj instanceof String) return obj != null && !"".equals(obj);
        if(obj instanceof Long) return obj != null;
        if(obj instanceof Integer) return obj != null;
        if(obj instanceof Map) {
            if(obj == null) return false;
            if(((Map)obj).isEmpty()) return false;
        }
        if(obj instanceof List){
            if(obj == null) return false;
            if(((List)obj).isEmpty()) return false;
            if(((List)obj).size() <= 0) return false;
        }
        return obj != null;
    }

    protected <T, K> T map(K o, Class<T> cls) {
        if(o == null) {
            try {
                return cls.getConstructor().newInstance();
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.convertValue(o, cls);
        }catch (Exception e){
            try {
                e.printStackTrace();
                return cls.getConstructor().newInstance();
            }catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }
    }

    protected <T, K> T map(K o, TypeReference<T> typeReference){
        ObjectMapper mapper = new ObjectMapper();
        if(o == null){
            String genericSuperclass = typeReference.getType().getTypeName();
            genericSuperclass = genericSuperclass.replaceAll("<.*>", "");
            try {
                Class.forName(genericSuperclass).getConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        return mapper.convertValue(o, typeReference);
    }

    public Map<String, ?> toMap(){
        return map(this, Map.class);
    }

}
