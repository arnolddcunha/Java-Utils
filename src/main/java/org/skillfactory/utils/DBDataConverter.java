package org.skillfactory.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import javax.persistence.AttributeConverter;

public class DBDataConverter<T> implements AttributeConverter<T, String> {

  private final TypeToken<T> typeToken = new TypeToken<T>(getClass()) {
  };
  private final Class<? super T> typeClass = typeToken.getRawType(); // or getRawType() to return Class<? super T>

  @Override
  public String convertToDatabaseColumn(T data) {
    if (data == null) {
      return null;
    }

    try {
      ObjectMapper mapper = new ObjectMapper();

      return mapper.writeValueAsString(data);

    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public T convertToEntityAttribute(String s) {
    if (s == null) {
      return null;
    }
    try {
      ObjectMapper mapper = new ObjectMapper();
      return (T) mapper.readValue(s, typeClass);

    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

}
