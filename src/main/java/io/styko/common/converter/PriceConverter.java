package io.styko.common.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.styko.common.persistance.Price;
import java.lang.reflect.Type;
import java.util.List;
import javax.persistence.AttributeConverter;
import org.springframework.beans.factory.annotation.Autowired;

public class PriceConverter implements AttributeConverter<List<Price>, String> {
  private final Gson gson;
  private static final Type type = new TypeToken<List<Price>>() {}.getType();

  @Autowired
  public PriceConverter(Gson gson) {
    this.gson = gson;
  }

  @Override
  public String convertToDatabaseColumn(List<Price> prices) {
    return gson.toJson(prices);
  }

  @Override
  public List<Price> convertToEntityAttribute(String stringJson) {
    return gson.fromJson(stringJson, type);
  }
}
