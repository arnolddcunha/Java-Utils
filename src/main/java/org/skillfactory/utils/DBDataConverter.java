package org.skillfactory.utils;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.reflect.TypeToken;

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
      return Functions.objectMapper().writeValueAsString(data);
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
      return (T) Functions.objectMapper().readValue(s, typeClass);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

}
