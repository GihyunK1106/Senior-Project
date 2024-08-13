package org.example.domain.types;

import lombok.Getter;

@Getter
public enum Categories {

    SOCCER("1", "soccer"), VOLLEY_BALL("2", "volley ball");

    private String typeCode;
    private String type;
    Categories(String typeCode, String type) {
        this.type = type;
        this.typeCode = typeCode;
    }

    public static Categories getType(String type){
        if(type == null)
            return null;
        for(Categories ut : Categories.values()){
            if(ut.getType().equals(type))
                return ut;
        }
        return null;
    }

    public static Categories getTypeCode(String type){
        if(type == null)
            return null;
        for(Categories ut : Categories.values()){
            if(ut.getTypeCode().equals(type))
                return ut;
        }
        return null;
    }
}
