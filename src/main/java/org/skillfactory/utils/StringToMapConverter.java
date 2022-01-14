package org.skillfactory.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.HashMap;
import java.util.Map;

@Converter
public class StringToMapConverter implements AttributeConverter<Map<String, Object>, String> {

  @Override
  public String convertToDatabaseColumn(Map<String, Object> data) {
    if (data == null)
      return null;

    try {
      return Functions.objectMapper().writeValueAsString(data);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Map<String, Object> convertToEntityAttribute(String dbData) {
    if (dbData == null)
      return null;
    try {
      // this is an un-modifiable map
      Map<String, Object> mapData = Functions.objectMapper().readValue(dbData,
          new TypeReference<Map<String, Object>>() {
          });
      // returning a modifiable map with contents of the unmodifiable map
      return new HashMap<>(mapData);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}