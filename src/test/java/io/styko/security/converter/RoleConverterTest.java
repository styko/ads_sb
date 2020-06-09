package io.styko.security.converter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.google.gson.Gson;
import io.styko.common.config.GsonConfig;
import io.styko.security.persistance.ERole;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;

class RoleConverterTest {

  public static final String ROLES_CONVERTED_TO_STRING = "[\"ROLE_USER\",\"ROLE_ADMIN\"]";
  public static final Set<ERole> ROLES = new TreeSet<>(List.of(ERole.ROLE_USER, ERole.ROLE_ADMIN)) ;
  private final Gson gson = new GsonConfig().gson();

  private RoleConverter roleConverter = new RoleConverter(gson);

  @Test
  void convertToDatabaseColumn() {
    String converted = roleConverter.convertToDatabaseColumn(ROLES);
    assertThat(converted).isEqualTo(ROLES_CONVERTED_TO_STRING);
  }

  @Test
  void convertToEntityAttribute() {
    Set<ERole> convertedRoles = roleConverter.convertToEntityAttribute(ROLES_CONVERTED_TO_STRING);
    assertThat(convertedRoles).isEqualTo(ROLES);
  }
}
