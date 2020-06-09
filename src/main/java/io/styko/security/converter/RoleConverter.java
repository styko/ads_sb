package io.styko.security.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.styko.security.persistance.ERole;
import java.lang.reflect.Type;
import java.util.Set;
import javax.persistence.AttributeConverter;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleConverter implements AttributeConverter<Set<ERole>, String> {
  private final Gson gson;
  private static final Type type = new TypeToken<Set<ERole>>() {}.getType();

  @Autowired
  public RoleConverter(Gson gson) {
    this.gson = gson;
  }

  @Override
  public String convertToDatabaseColumn(Set<ERole> prices) {
    return gson.toJson(prices);
  }

  @Override
  public Set<ERole> convertToEntityAttribute(String stringJson) {
    return gson.fromJson(stringJson, type);
  }
}
