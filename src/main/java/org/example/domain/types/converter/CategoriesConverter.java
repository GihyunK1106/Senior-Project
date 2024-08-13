package org.example.domain.types.converter;

import org.example.domain.types.Categories;

import javax.persistence.AttributeConverter;

public class CategoriesConverter implements AttributeConverter<Categories, String> {
    @Override
    public String convertToDatabaseColumn(Categories attribute) {
        return attribute != null ? attribute.getType() : null;
    }

    @Override
    public Categories convertToEntityAttribute(String dbData) {
        if(dbData == null)
            return null;
        return Categories.getType(dbData);
    }
}
