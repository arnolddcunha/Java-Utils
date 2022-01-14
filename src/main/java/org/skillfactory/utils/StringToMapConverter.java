package org.skillfactory.utils;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;


@Converter
public class StringToMapConverter implements AttributeConverter<Map<String,Object>, String> {

    @Override
    public String convertToDatabaseColumn(Map<String,Object> attribute) {
        if (attribute == null)
            return null;

        try {
            ObjectMapper mapper = new ObjectMapper();

            return mapper.writeValueAsString(attribute);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String,Object> convertToEntityAttribute(String dbData) {
        if(dbData == null)
            return null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            
            Map<String, Object> mapData = mapper.readValue(dbData, new TypeReference<Map<String, Object>>() {});
            return new HashMap<>(mapData);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}